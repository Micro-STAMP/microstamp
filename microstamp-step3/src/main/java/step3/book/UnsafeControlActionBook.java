package step3.book;

public class UnsafeControlActionBook {

    public static final String FIND_MAX_UCA_CODE_NUMBER_BY_ANALYSIS_ID = """
        SELECT COALESCE(MAX(CAST(SUBSTRING(uca.ucaCode, 5) AS int)), 0)
        FROM UnsafeControlAction uca
        WHERE uca.analysisId = :analysisId
        AND uca.ucaCode LIKE 'UCA-%'
        """;

}
