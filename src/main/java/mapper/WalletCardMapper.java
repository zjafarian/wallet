package mapper;

import model.WalletCard;
import org.mapstruct.Mapper;
import service.wallet.model.WalletCardRequest;

@Mapper(componentModel = "spring")
public interface WalletCardMapper {

    WalletCard toWalletCard(WalletCardRequest debitCardRequest);
}
