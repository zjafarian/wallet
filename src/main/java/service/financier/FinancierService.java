package service.financier;


import lombok.extern.log4j.Log4j2;
import model.Financier;
import org.springframework.stereotype.Service;
import repository.FinancierRepository;


@Service
@Log4j2
public class FinancierService {

    private final FinancierRepository financierRepository;


    public FinancierService(FinancierRepository financierRepository) {
        this.financierRepository = financierRepository;
    }

    public Financier saveFinancier(Financier financier){
        return financierRepository.save(financier);
    }
}