package mapper;

import model.Financier;
import org.mapstruct.Mapper;
import service.wallet.model.FinancierRequest;

@Mapper(componentModel = "spring")
public interface FinancierMapper {

    Financier toFinancier(FinancierRequest financierRequest);
    FinancierRequest toFinancierRequest(Financier financier);
}
