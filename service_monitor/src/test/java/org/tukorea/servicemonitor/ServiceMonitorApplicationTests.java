package org.tukorea.servicemonitor;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ServiceMonitorApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void dashboardLoads() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Service Monitor Dashboard")))
                .andExpect(content().string(containsString("Failure Injection Status")))
                .andExpect(content().string(containsString("Library Service Runtime")));
    }

    @Test
    void healthSummaryIncludesLibraryAndPostgresTargets() throws Exception {
        mockMvc.perform(get("/api/health/summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.target=='LIBRARY_SERVICE')].status").value(hasItem("DOWN")))
                .andExpect(jsonPath("$[?(@.target=='POSTGRES')].status").value(hasItem("UP")));
    }

    @Test
    void failureInjectionRedirectsBackToDashboardWhenDisabled() throws Exception {
        mockMvc.perform(post("/failure-injection")
                        .param("target", "LIBRARY_SERVICE")
                        .param("command", "RESTART")
                        .param("reason", "test verification"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/#tab-failure-injection"))
                .andExpect(flash().attributeExists("controlResult"));
    }
}
