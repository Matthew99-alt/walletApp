package com.walletApp.service;

import com.walletApp.encoder.WalletEncoder;
import com.walletApp.enums.OperationTypes;
import com.walletApp.exception.NotEnoughCashException;
import com.walletApp.exception.OperationTypeNotFoundException;
import com.walletApp.exception.WalletNotFoundException;
import com.walletApp.mapper.WalletMapper;
import com.walletApp.model.dto.WalletDTO;
import com.walletApp.model.entity.Wallet;
import com.walletApp.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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
        UUID walletId = walletEncoder.decodeUUID(walletDTO.getWalletId());
        Optional<Wallet> existingWallet = walletRepository.findById(walletId);

        if (!operationTypeList.contains(walletDTO.getOperationType())){
            throw new OperationTypeNotFoundException("Указанной операции нет в списке");
        }

        if (walletRepository.findById(walletId).isEmpty()&&
                walletDTO.getOperationType()==OperationTypes.WITHDRAW){
            throw new WalletNotFoundException("Указанный кошелёк не найден");
        }

        if (walletRepository.findById(walletId).isEmpty()&&
        walletDTO.getOperationType()==OperationTypes.DEPOSIT) {
            walletRepository.save(walletMapper.makeAWallet(walletDTO));
        }

            if(existingWallet.isPresent()) {
                Wallet wallet = existingWallet.get();
                if (walletDTO.getOperationType()==OperationTypes.DEPOSIT) {
                    wallet.setAmount(wallet.getAmount()+walletDTO.getAmount());
                }
                if(walletDTO.getOperationType()==OperationTypes.WITHDRAW){
                    if(walletDTO.getAmount()>wallet.getAmount()){
                        throw new NotEnoughCashException("Недостаточно средств");
                    }
                    wallet.setAmount(wallet.getAmount()-walletDTO.getAmount());
                }
                walletRepository.save(wallet);
            }
        return walletMapper.makeAWalletDTO(walletRepository.findById(walletId).get());
        }

    }