package service.profile;


import exception.BusinessException;
import lombok.extern.log4j.Log4j2;
import mapper.ProfileMapper;
import model.Profile;
import model.Wallet;
import org.springframework.stereotype.Service;
import repository.ProfileRepository;
import repository.WalletRepository;
import service.model.response.ApiResponse;
import service.profile.model.ProfileRequest;
import service.wallet.WalletService;

import java.util.Optional;


@Service
@Log4j2
public class ProfileService {
    private final ProfileRepository profileRepository;

    private final WalletService walletService;
    private final ProfileMapper profileMapper;

    public ProfileService(ProfileRepository profileRepository,
                          WalletService walletService,
                          ProfileMapper profileMapper) {
        this.profileRepository = profileRepository;
        this.walletService = walletService;
        this.profileMapper = profileMapper;
    }


    public ApiResponse createProfile(ProfileRequest profileRequest) {
        log.info("ProfileService ,createProfile , profileRequest :{}", profileRequest);
        var result = getProfileByNationalCode(profileRequest.getNationalCode()).map((pr) -> new ApiResponse(false,
                pr,
                "exists Profile by nationalCode:" + pr.getNationalCode(),
                ""
        )).orElseGet(() -> {
            var profile = saveProfile(profileMapper.toProfile(profileRequest));
            var wallet = saveWallet(Wallet.builder()
                    .title("DefaultWallet")
                    .totalBalance(0L)
                    .profile(profile)
                    .build());


            return new ApiResponse(true,profile);
        });

        log.info("ProfileService ,createProfile , result :{}", result);

      return result;

    }

    private Wallet saveWallet(Wallet wallet) {
        log.info("ProfileService ,saveWallet , wallet :{}", wallet);
        var result = walletService.saveWallet(wallet);

        log.info("ProfileService ,saveWallet , result :{}", result);
        return result;
    }

    public Optional<Profile> getProfileByNationalCode(String nationalCode) {
        log.info("ProfileService ,getProfileByNationalCode , nationalCode :{}", nationalCode);
        var result = profileRepository.findProfileByNationalCodeAndIsDeletedFalse(nationalCode);

        log.info("ProfileService ,getProfileByNationalCode , result :{}", result);
        return result;
    }

    public Profile saveProfile(Profile profile) {
        log.info("ProfileService ,saveProfile , profile :{}", profile);
        var result = profileRepository.save(profile);

        log.info("ProfileService ,saveProfile , result :{}", result);
        return result;
    }


    public Profile findProfileActiveById(Long profileId){
        log.info("ProfileService ,findProfileActiveById , profileId :{}", profileId);
        var result = profileRepository.findProfileByIdAndIsDeletedIsFalseAndIsActiveIsTrue(profileId)
                .orElseThrow(() -> new BusinessException("profile isn't active by this id :" + profileId, 400010));

        log.info("ProfileService ,findProfileActiveById , result :{}", result);
      return result;
    }



}