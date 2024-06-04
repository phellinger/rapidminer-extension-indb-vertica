/**
 * @author phellinger
 */
package com.rapidminer.extension.indbother.vertica;

import java.sql.Types;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import com.rapidminer.extension.indatabase.db.step.DbStep;
import com.rapidminer.extension.indatabase.db.step.Filter;
import com.rapidminer.extension.indatabase.db.step.Filter.FilterCondition;
import com.rapidminer.extension.indatabase.db.step.Join;
import com.rapidminer.extension.indatabase.db.step.ProbabilitySample;
import com.rapidminer.extension.indatabase.operator.function.FunctionDefinition;
import com.rapidminer.extension.indatabase.provider.DatabaseProvider;
import com.rapidminer.extension.indatabase.provider.DatabaseProviderFactory;
import com.rapidminer.extension.indatabase.provider.DatabaseProviderFactory.DatabaseProviderDescriptor;
import com.rapidminer.extension.indatabase.sql.SqlSyntax;
import com.rapidminer.extension.indatabase.sql.postgresql.ProbabilitySamplePostgreSql;
import com.rapidminer.extension.indatabase.sql.shared.JoinFullOuterSql;
import com.rapidminer.tools.Ontology;


/**
 * Customizes some of the default provider methods to demonstrate setting the ID, describing custom
 * aggregation functions and implementing metadata retrieval methods.
 *
 * @author phellinger
 */
public enum VerticaProvider implements DatabaseProvider {

    INSTANCE;

    private static final String PROVIDER_ID = "vertica";
    private static final Map<String, FunctionDefinition> AGGREGATEFUNCTIONS = new LinkedHashMap<>();

    static {
        DatabaseProviderFactory
                .registerProvider(new DatabaseProviderDescriptor(INSTANCE, 10, "jdbc:vertica:"));

        FunctionDefinition[] fs = new FunctionDefinition[] {

                new FunctionDefinition("APPROXIMATE_MEDIAN", "approximate median",
                        "approximate median of an expression over a group of rows", Ontology.REAL),
                new FunctionDefinition("AVG", "average", "the average (arithmetic mean) of all input values",
                        FunctionDefinition.OUTPUT_TYPE_SAME_AS_INPUT),
                new FunctionDefinition("AVG(DISTINCT)", "average (distinct rows)",
                        "the average of the distinct values of the argument.", FunctionDefinition.OUTPUT_TYPE_SAME_AS_INPUT),
                new FunctionDefinition("BIT_AND", "bitwise and",
                        "the bitwise AND of all non-null input values, or null if none",
                        FunctionDefinition.OUTPUT_TYPE_SAME_AS_INPUT),
                new FunctionDefinition("BIT_OR", "bitwise or",
                        "the bitwise OR of all non-null input values, or null if none",
                        FunctionDefinition.OUTPUT_TYPE_SAME_AS_INPUT),
                new FunctionDefinition("BIT_XOR", "bitwise xor",
                        "the bitwise XOR of all non-null input values, or null if none",
                        FunctionDefinition.OUTPUT_TYPE_SAME_AS_INPUT),
                new FunctionDefinition("BOOL_AND", "logical and", "true if all input values are true, otherwise false",
                        Ontology.BINOMINAL),
                new FunctionDefinition("BOOL_OR", "logical or", "true if at least one input value is true, otherwise false",
                        Ontology.BINOMINAL),
                new FunctionDefinition("BOOL_XOR", "logical xor", "true if at only one input value is true, otherwise false",
                        Ontology.BINOMINAL),
                new FunctionDefinition("COUNT", "count", "number of input rows", Ontology.INTEGER),
                new FunctionDefinition("COUNT(DISTINCT)", "count (distinct rows)",
                        "the count of a number of different values.", Ontology.INTEGER),
                new FunctionDefinition("MAX", "maximum", "maximum value of expression across all input values",
                        FunctionDefinition.OUTPUT_TYPE_SAME_AS_INPUT),
                new FunctionDefinition("MIN", "minimum", "minimum value of expression across all input values",
                        FunctionDefinition.OUTPUT_TYPE_SAME_AS_INPUT),
                new FunctionDefinition("SUM", "sum", "sum of expression across all input values",
                        FunctionDefinition.OUTPUT_TYPE_SAME_AS_INPUT),
                new FunctionDefinition("SUM_FLOAT", "sum float", "sum of expression across all input values, always as float",
                        Ontology.REAL),
                new FunctionDefinition("STDDEV_POP", "standard deviation",
                        "population standard deviation of the input values", FunctionDefinition.OUTPUT_TYPE_SAME_AS_INPUT),
                new FunctionDefinition("STDDEV_SAMP", "sample standard deviation",
                        "sample standard deviation of the input values", FunctionDefinition.OUTPUT_TYPE_SAME_AS_INPUT),
                new FunctionDefinition("VAR_POP", "variance",
                        "population variance of the input values (square of the population standard deviation)",
                        FunctionDefinition.OUTPUT_TYPE_SAME_AS_INPUT),
                new FunctionDefinition("VAR_SAMP", "sample variance",
                        "sample variance of the input values (square of the sample standard deviation)",
                        FunctionDefinition.OUTPUT_TYPE_SAME_AS_INPUT),
    };
		Stream.of(fs).forEachOrdered(f -> AGGREGATEFUNCTIONS.put(f.getName(), f));
    }

    @Override
    public Map<String, FunctionDefinition> getAggregationFunctions() {
        return AGGREGATEFUNCTIONS;
    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }

    @Override
    public boolean supportsProjects() {
        return false;
    }

	@Override
	public Map<Class<? extends DbStep>, SqlSyntax<?>> getDbStepToSyntaxMap() {
		Map<Class<? extends DbStep>, SqlSyntax<?>> res = DatabaseProvider.super.getDbStepToSyntaxMap();
		res.put(Join.class, new JoinFullOuterSql());
        res.put(ProbabilitySample.class, new ProbabilitySamplePostgreSql());
		return res;
	}

    @Override
    public Map<String, Integer> getDataTypeSuggestions() {
        Map<String, Integer> res = new LinkedHashMap<>();
        res.put("BIGINT", Types.DOUBLE);
        res.put("BOOLEAN", Types.VARCHAR);
        res.put("DATE", Types.DATE);
        res.put("DATETIME", Types.TIMESTAMP);
        res.put("DOUBLE PRECISION", Types.DOUBLE);
        res.put("FLOAT", Types.DOUBLE);
        res.put("INTEGER", Types.INTEGER);
        res.put("NUMERIC", Types.NUMERIC);
        res.put("REAL", Types.DOUBLE);
        res.put("SMALLINT", Types.INTEGER);
        res.put("VARCHAR", Types.VARCHAR);
        res.put("TIME", Types.TIME);
        res.put("TIMESTAMP", Types.TIMESTAMP);
        return res;
    }

    @Override
	public Map<Filter.FilterCondition, BiFunction<String, String, String>> getFilterSyntax() {
		Map<Filter.FilterCondition, BiFunction<String, String, String>> res = DatabaseProvider.super.getFilterSyntax();

		// need to use REGEXP_LIKE function
		res.put(FilterCondition.MATCHES, (col, val) -> "REGEXP_LIKE(" + col + ", " + literal("^" + val + "$") + ")");
		return res;
	}
}
