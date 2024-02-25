package service.merchant;


import exception.BusinessException;
import lombok.extern.log4j.Log4j2;
import model.Merchant;
import model.Transaction;
import org.springframework.stereotype.Service;
import repository.MerchantRepository;


@Service
@Log4j2
public class MerchantService {

    final private MerchantRepository merchantRepository;


    public MerchantService(MerchantRepository merchantRepository) {
        this.merchantRepository = merchantRepository;
    }

    public Merchant findActiveMerchantById(Long merchantId) {
       return merchantRepository.findMerchantByIdAndIsDeletedIsFalseAndIsActiveIsTrue(merchantId)
               .orElseThrow(() -> new BusinessException("merchant isn't active by this id :" +
                       merchantId, 600010));

    }

    public void callVerifyMercahntService(Transaction transaction) {
    }
}