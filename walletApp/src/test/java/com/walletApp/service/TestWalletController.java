package com.walletApp.service;

import com.walletApp.config.PostgresContainer;
import com.walletApp.encoder.WalletEncoder;
import com.walletApp.enums.OperationTypes;
import com.walletApp.model.entity.Wallet;
import com.walletApp.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
public class TestWalletController extends PostgresContainer {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WalletEncoder walletEncoder;

    @Autowired
    private WalletRepository walletRepository;

    @BeforeEach
    void setUp() {
        walletRepository.deleteAll();
    }

    private Wallet createWallet() {
        Wallet wallet = new Wallet();
        wallet.setWalletId(UUID.randomUUID());
        wallet.setOperationType(OperationTypes.DEPOSIT);
        wallet.setAmount(5000);
        walletRepository.save(wallet);

        return wallet;
    }

    @Test
    void getWallet_ShouldReturnWallet_WhenExists() throws Exception {
        Wallet wallet = createWallet();
        String testWalletId = walletEncoder.encodeUUID(wallet.getWalletId());

        mockMvc.perform(get("/api/v1/")
                        .param("id", testWalletId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getWallet_ShouldReturnException_WhenNotExists() throws Exception {
        String nonExistingId = walletEncoder.encodeUUID(UUID.randomUUID());

        mockMvc.perform(get("/api/v1/")
                        .param("id", nonExistingId))
                .andExpect(status().isNotFound());
    }

    @Test
    void getWallet_ShouldReturnBadRequest_WhenInvalidIdFormat() throws Exception {
        mockMvc.perform(get("/api/v1/")
                        .param("id", "invalid_id_format"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void saveNewWallet() throws Exception {
        String requestBody = "{\n" +
                "  \"walletId\": \"f47-33-4372-67-09\",\n" +
                "  \"operationType\": \"DEPOSIT\",\n" +
                "  \"amount\": 500\n" +
                "}";

        mockMvc.perform(
                        post("/api/v1/save").contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    void depositForAnExistingWallet() throws Exception {
        Wallet wallet = createWallet();
        String testWalletId = walletEncoder.encodeUUID(wallet.getWalletId());

        String requestBody = "{\n" +
                "  \"walletId\":\"" + testWalletId + "\",\n" +
                "  \"operationType\": \"DEPOSIT\",\n" +
                "  \"amount\": 1000\n" +
                "}";

        mockMvc.perform(
                        post("/api/v1/save").contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    void savingWalletWithWrongUUID() throws Exception {
        String requestBody = "{\n" +
                "  \"walletId\":\"" + "f47--4372-67-09" + "\",\n" +
                "  \"operationType\": \"DEPOSIT\",\n" +
                "  \"amount\": 1000\n" +
                "}";

        mockMvc.perform(
                        post("/api/v1/save").contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    void saveNewWalletWithNegativeBalance() throws Exception {
        String requestBody = "{\n" +
                "  \"walletId\": \"f47-33-4372-67-09\",\n" +
                "  \"operationType\": \"DEPOSIT\",\n" +
                "  \"amount\": -500\n" +
                "}";

        mockMvc.perform(
                        post("/api/v1/save").contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    void saveNewWalletWithNonExistingOperationType() throws Exception {
        String requestBody = "{\n" +
                "  \"walletId\": \"f47-33-4372-67-09\",\n" +
                "  \"operationType\": \"IDUNNO\",\n" +
                "  \"amount\": -500\n" +
                "}";

        mockMvc.perform(
                        post("/api/v1/save").contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    void withdrawForAnExistingWallet() throws Exception {
        Wallet wallet = createWallet();
        String testWalletId = walletEncoder.encodeUUID(wallet.getWalletId());

        String requestBody = "{\n" +
                "  \"walletId\":\"" + testWalletId + "\",\n" +
                "  \"operationType\": \"WITHDRAW\",\n" +
                "  \"amount\": 1000\n" +
                "}";

        mockMvc.perform(
                        post("/api/v1/save").contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    void notEnoughCashExceptionTest() throws Exception {
        Wallet wallet = createWallet();
        String testWalletId = walletEncoder.encodeUUID(wallet.getWalletId());

        String requestBody = "{\n" +
                "  \"walletId\":\"" + testWalletId + "\",\n" +
                "  \"operationType\": \"WITHDRAW\",\n" +
                "  \"amount\": 50000\n" +
                "}";

        mockMvc.perform(
                        post("/api/v1/save").contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    void nonExistingWalletTest() throws Exception {
        String requestBody = "{\n" +
                "  \"walletId\":\"" + UUID.randomUUID() + "\",\n" +
                "  \"operationType\": \"WITHDRAW\",\n" +
                "  \"amount\": 500\n" +
                "}";

        mockMvc.perform(
                        post("/api/v1/save").contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                );
    }


}