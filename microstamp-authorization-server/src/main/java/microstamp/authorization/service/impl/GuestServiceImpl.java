package microstamp.authorization.service.impl;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import microstamp.authorization.dto.AnalysisReadDto;
import microstamp.authorization.dto.ExportReadDto;
import microstamp.authorization.exception.NotFoundException;
import microstamp.authorization.service.AnalysisService;
import microstamp.authorization.service.ExportService;
import microstamp.authorization.service.GuestService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class GuestServiceImpl implements GuestService {

    @Value("${security.issuer-uri}")
    private String issuerUri;

    private final AnalysisService analysisService;

    private final ExportService exportService;

    private final JwtEncoder jwtEncoder;

    public List<AnalysisReadDto> findAll() {
        return analysisService.findGuestAnalyses();
    }

    public AnalysisReadDto findById(UUID analysisId) {
        validateGuestAnalysis(analysisId);
        return analysisService.findById(analysisId);
    }

    public List<ExportReadDto> exportAll() {
        return analysisService.findGuestAnalyses().stream()
                .map(a -> exportService.exportToJson(a.getId(), getGuestToken()))
                .toList();
    }

    public ExportReadDto exportToJson(UUID analysisId) {
        validateGuestAnalysis(analysisId);
        return exportService.exportToJson(analysisId, getGuestToken());
    }

    public byte[] exportToPdf(UUID analysisId) throws IOException {
        validateGuestAnalysis(analysisId);
        return exportService.exportToPdf(analysisId, getGuestToken());
    }

    private void validateGuestAnalysis(UUID analysisId) {
        if (analysisService.findGuestAnalyses().stream().noneMatch(a -> a.getId().equals(analysisId)))
            throw new NotFoundException("Guest Analysis", analysisId.toString());
    }

    private String getGuestToken() {
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(issuerUri)
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(10))
                .subject("guest-request")
                .audience(List.of("guest-request"))
                .build();

        String jwt = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return "Bearer " + jwt;
    }
}