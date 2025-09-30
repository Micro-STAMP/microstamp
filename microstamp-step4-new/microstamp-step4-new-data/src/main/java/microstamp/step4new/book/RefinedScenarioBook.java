package microstamp.step4new.book;

public class RefinedScenarioBook {

    public static final String FIND_MAX_CODE_NUMBER_BY_ANALYSIS_ID = """
        SELECT COALESCE(MAX(CAST(SUBSTRING(rs.code, 5) AS int)), 0)
        FROM RefinedScenario rs
        JOIN rs.formalScenarioClass fsc
        JOIN fsc.formalScenario fs
        WHERE fs.analysisId = :analysisId
        AND rs.code LIKE 'RSC-%'
        """;

}
