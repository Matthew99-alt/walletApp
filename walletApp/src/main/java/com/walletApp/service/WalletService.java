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
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;
    private final WalletMapper walletMapper;
    private final WalletEncoder walletEncoder;

    public WalletDTO getWallet(String UUID) {
        return walletMapper.makeWalletDTO(
                walletRepository.getReferenceById(walletEncoder.decodeUUID(UUID))
        );
    }

    public WalletDTO saveWallet(WalletDTO walletDTO) {
        List<OperationTypes> operationTypeList = Arrays.asList(OperationTypes.values());
        UUID walletId = walletEncoder.decodeUUID(walletDTO.getWalletId());

        validateWalletDto(walletDTO, operationTypeList, walletId);

        //TODO: Optional посмотреть еще раз
        Wallet existingWallet = walletRepository.findById(walletId).orElse(createNewWallet(walletDTO));

        //DEPOSIT и WITHDRAW позволяют работать сращу с деньгами, причем!!! на кредитном кошельке сразу есть деньги!! а на депозитном нет!
        if (walletDTO.getOperationType() == OperationTypes.DEPOSIT) {
            existingWallet.setAmount(existingWallet.getAmount() + walletDTO.getAmount());
        }
        if (walletDTO.getOperationType() == OperationTypes.WITHDRAW) {
            if (walletDTO.getAmount() > existingWallet.getAmount()) {
                throw new NotEnoughCashException("Недостаточно средств");
            }
            existingWallet.setAmount(existingWallet.getAmount() - walletDTO.getAmount());
        }
        walletRepository.save(existingWallet);

        return walletMapper.makeWalletDTO(existingWallet);
    }

    //TODO: либо создавать, но что тогда с балансом??
    //TODO: либо просто ругаться
    private Wallet createNewWallet(WalletDTO walletDTO) {
        return walletRepository.save(walletMapper.makeWallet(walletDTO));
    }


    private void validateWalletDto(WalletDTO walletDTO, List<OperationTypes> operationTypeList, UUID walletId) {
        if (!operationTypeList.contains(walletDTO.getOperationType())) {
            throw new OperationTypeNotFoundException("Указанной операции нет в списке");
        }

        if (walletRepository.findById(walletId).isEmpty() &&
                walletDTO.getOperationType() == OperationTypes.WITHDRAW) {
            throw new WalletNotFoundException("Указанный кошелёк не найден");
        }

        if (walletRepository.findById(walletId).isEmpty() &&
                walletDTO.getOperationType() == OperationTypes.DEPOSIT) {
            walletRepository.save(walletMapper.makeWallet(walletDTO));
        }

        if (walletDTO.getAmount() < 0) {
            throw new NotEnoughCashException("Баланс кошелька не может быть отрицательным");
        }
    }

}