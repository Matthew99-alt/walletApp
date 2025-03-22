package com.walletApp.service;

import com.walletApp.encoder.WalletEncoder;
import com.walletApp.enums.OperationTypes;
import com.walletApp.exception.OperationTypeNotFoundException;
import com.walletApp.mapper.WalletMapper;
import com.walletApp.model.dto.WalletDTO;
import com.walletApp.model.entity.Wallet;
import com.walletApp.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WalletService {
    private final WalletRepository walletRepository;

    private final WalletMapper walletMapper;

    private final WalletEncoder walletEncoder;

    public WalletDTO getWallet(String UUID){
        return walletMapper.makeAWalletDTO(
                walletRepository.getReferenceById(walletEncoder.decodeUUID(UUID))
        );
    }

    public WalletDTO saveWallet(WalletDTO walletDTO){
        OperationTypes[] operationTypes = OperationTypes.values();
        List<OperationTypes> operationTypeList = Arrays.asList(operationTypes);
        if (operationTypeList.contains(walletDTO.getOperationType())) {
            Wallet savedWallet = walletRepository.save(walletMapper.makeAWallet(walletDTO));
            return walletMapper.makeAWalletDTO(savedWallet);
        } else {
            throw new OperationTypeNotFoundException("Указанной операции нет в списке");
        }
    }
}
