package microstamp.cast.step1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import microstamp.cast.step1.dto.casthazard.CastHazardUpdateDto;
import microstamp.cast.step1.service.CastHazardService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CastHazardController.class)
class CastHazardControllerTest {

    private static final String BASE_URL = "/cast/hazards";

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private CastHazardService service;

    @MockBean
    private JpaMetamodelMappingContext jpaMetamodelMappingContext;

    @Test
    @WithMockUser
    @DisplayName("PUT /cast/hazards/{id} deve retornar 409 quando o código já estiver em uso na análise")
    void updateShouldReturnConflictWhenCodeIsDuplicated() throws Exception {
        UUID id = UUID.randomUUID();

        CastHazardUpdateDto updateDto = CastHazardUpdateDto.builder()
                .code("H-2")
                .name("Hazard")
                .accidentLossEventIds(List.of())
                .build();

        doThrow(new DataIntegrityViolationException("could not execute statement",
                new SQLIntegrityConstraintViolationException("Duplicate entry 'H-2-" + UUID.randomUUID() + "' for key 'cast_hazards.UK_code_analysis'")))
                .when(service).update(ArgumentMatchers.eq(id), ArgumentMatchers.any(CastHazardUpdateDto.class));

        mockMvc.perform(put(BASE_URL + "/{id}", id)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errors[0].key").value("DuplicatedCode"))
                .andExpect(jsonPath("$.errors[0].message").value("This code is already in use in this analysis."));
    }
}
