package microstamp.cast.step1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import microstamp.cast.step1.dto.systemdescription.SystemDescriptionInsertDto;
import microstamp.cast.step1.dto.systemdescription.SystemDescriptionReadDto;
import microstamp.cast.step1.dto.systemdescription.SystemDescriptionUpdateDto;
import microstamp.cast.step1.service.SystemDescriptionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;


import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SystemDescriptionController.class)
class SystemDescriptionControllerTest {


    private static final String BASE_URL = "/cast/system-descriptions";

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private SystemDescriptionService service;

    @Test
    @WithMockUser
    @DisplayName("GET /cast/system-descriptions deve retornar todas as descrições do sistema")
    void findAllShouldReturnAllSystemDescriptions() throws Exception {
        UUID id = UUID.randomUUID();

        SystemDescriptionReadDto readDto = SystemDescriptionReadDto.builder()
                .id(id)
                .code("SD-001")
                .description("Descrição do sistema")
                .analysisBoundary("Fronteira da análise")
                .build();

        when(service.findAll()).thenReturn(List.of(readDto));

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(id.toString()))
                .andExpect(jsonPath("$[0].code").value("SD-001"))
                .andExpect(jsonPath("$[0].description").value("Descrição do sistema"))
                .andExpect(jsonPath("$[0].analysisBoundary").value("Fronteira da análise"));

        verify(service).findAll();
    }

    @Test
    @WithMockUser
    @DisplayName("GET /cast/system-descriptions/{id} deve retornar uma descrição por ID")
    void findByIdShouldReturnSystemDescription() throws Exception {
        UUID id = UUID.randomUUID();

        SystemDescriptionReadDto readDto = SystemDescriptionReadDto.builder()
                .id(id)
                .code("SD-002")
                .description("Descrição encontrada por ID")
                .analysisBoundary("Boundary")
                .build();

        when(service.findById(id)).thenReturn(readDto);

        mockMvc.perform(get(BASE_URL + "/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.code").value("SD-002"))
                .andExpect(jsonPath("$.description").value("Descrição encontrada por ID"))
                .andExpect(jsonPath("$.analysisBoundary").value("Boundary"));

        verify(service).findById(id);
    }

    @Test
    @WithMockUser
    @DisplayName("GET /cast/system-descriptions/analysis/{id} deve retornar descrições por ID da análise")
    void findByAnalysisIdShouldReturnSystemDescriptions() throws Exception {
        UUID analysisId = UUID.randomUUID();
        UUID systemDescriptionId = UUID.randomUUID();

        SystemDescriptionReadDto readDto = SystemDescriptionReadDto.builder()
                .id(systemDescriptionId)
                .code("SD-003")
                .description("Descrição vinculada à análise")
                .analysisBoundary("Escopo da análise")
                .build();

        when(service.findByAnalysisId(analysisId)).thenReturn(List.of(readDto));

        mockMvc.perform(get(BASE_URL + "/analysis/{id}", analysisId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(systemDescriptionId.toString()))
                .andExpect(jsonPath("$[0].code").value("SD-003"))
                .andExpect(jsonPath("$[0].description").value("Descrição vinculada à análise"))
                .andExpect(jsonPath("$[0].analysisBoundary").value("Escopo da análise"));

        verify(service).findByAnalysisId(analysisId);
    }

    @Test
    @WithMockUser
    @DisplayName("POST /cast/system-descriptions deve criar uma descrição do sistema")
    void insertShouldCreateSystemDescription() throws Exception {
        UUID analysisId = UUID.randomUUID();
        UUID systemDescriptionId = UUID.randomUUID();

        SystemDescriptionInsertDto insertDto = SystemDescriptionInsertDto.builder()
                .code("SD-004")
                .description("Nova descrição")
                .analysisBoundary("Nova fronteira")
                .analysisId(analysisId)
                .build();

        SystemDescriptionReadDto readDto = SystemDescriptionReadDto.builder()
                .id(systemDescriptionId)
                .code("SD-004")
                .description("Nova descrição")
                .analysisBoundary("Nova fronteira")
                .build();

        when(service.insert(ArgumentMatchers.any(SystemDescriptionInsertDto.class))).thenReturn(readDto);

        mockMvc.perform(post(BASE_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(insertDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(systemDescriptionId.toString()))
                .andExpect(jsonPath("$.code").value("SD-004"))
                .andExpect(jsonPath("$.description").value("Nova descrição"))
                .andExpect(jsonPath("$.analysisBoundary").value("Nova fronteira"));

        verify(service).insert(ArgumentMatchers.any(SystemDescriptionInsertDto.class));
    }

    @Test
    @WithMockUser
    @DisplayName("PUT /cast/system-descriptions/{id} deve atualizar uma descrição do sistema")
    void updateShouldUpdateSystemDescription() throws Exception {
        UUID id = UUID.randomUUID();

        SystemDescriptionUpdateDto updateDto = SystemDescriptionUpdateDto.builder()
                .code("SD-005")
                .description("Descrição atualizada")
                .analysisBoundary("Fronteira atualizada")
                .build();

        doNothing().when(service).update(ArgumentMatchers.eq(id), ArgumentMatchers.any(SystemDescriptionUpdateDto.class));

        mockMvc.perform(put(BASE_URL + "/{id}", id)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk());

        verify(service).update(ArgumentMatchers.eq(id), ArgumentMatchers.any(SystemDescriptionUpdateDto.class));
    }

    @Test
    @WithMockUser
    @DisplayName("DELETE /cast/system-descriptions/{id} deve remover uma descrição do sistema")
    void deleteShouldDeleteSystemDescription() throws Exception {
        UUID id = UUID.randomUUID();

        doNothing().when(service).delete(id);

        mockMvc.perform(delete(BASE_URL + "/{id}", id)
                        .with(csrf()))
                .andExpect(status().isOk());

        verify(service).delete(id);
    }

    @Test
    @WithMockUser
    @DisplayName("POST /cast/system-descriptions deve retornar 400 quando campos obrigatórios forem inválidos")
    void insertShouldReturnBadRequestWhenBodyIsInvalid() throws Exception {
        SystemDescriptionInsertDto invalidDto = SystemDescriptionInsertDto.builder()
                .code("")
                .description("")
                .analysisId(null)
                .build();

        mockMvc.perform(post(BASE_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("PUT /cast/system-descriptions/{id} deve retornar 400 quando campos obrigatórios forem inválidos")
    void updateShouldReturnBadRequestWhenBodyIsInvalid() throws Exception {
        UUID id = UUID.randomUUID();

        SystemDescriptionUpdateDto invalidDto = SystemDescriptionUpdateDto.builder()
                .code("")
                .description("")
                .build();

        mockMvc.perform(put(BASE_URL + "/{id}", id)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }
}
