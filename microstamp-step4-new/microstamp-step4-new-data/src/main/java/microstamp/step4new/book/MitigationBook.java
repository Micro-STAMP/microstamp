package microstamp.step4new.book;

public class MitigationBook {

    public static final String FIND_MAX_CODE_NUMBER_BY_ANALYSIS_ID = """
        SELECT COALESCE(MAX(CAST(SUBSTRING(m.code, 6) AS int)), 0)
        FROM Mitigation m
        JOIN m.refinedScenario rs
        JOIN rs.formalScenarioClass fsc
        JOIN fsc.formalScenario fs
        WHERE fs.analysisId = :analysisId
        AND m.code LIKE 'RSOL-%'
        """;

}
