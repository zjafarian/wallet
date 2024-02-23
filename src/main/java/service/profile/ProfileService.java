package service.profile;


import lombok.extern.log4j.Log4j2;
import mapper.ProfileMapper;
import model.Profile;
import model.Wallet;
import org.springframework.stereotype.Service;
import repository.ProfileRepository;
import repository.WalletRepository;
import service.model.response.ApiResponse;
import service.profile.model.ProfileRequest;

import java.util.Objects;
import java.util.Optional;


@Service
@Log4j2
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final WalletRepository walletRepository;
    private final ProfileMapper profileMapper;

    public ProfileService(ProfileRepository profileRepository,
                          WalletRepository walletRepository,
                          ProfileMapper profileMapper) {
        this.profileRepository = profileRepository;
        this.walletRepository = walletRepository;
        this.profileMapper = profileMapper;
    }


    public ApiResponse createProfile(ProfileRequest profileRequest) {
      return getProfileByNationalCode(profileRequest.getNationalCode()).map((pr) -> new ApiResponse(false,
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

    }

    private Wallet saveWallet(Wallet wallet) {
        return walletRepository.save(wallet);
    }

    public Optional<Profile> getProfileByNationalCode(String nationalCode) {
        return profileRepository.findProfileByNationalCodeAndIsDeletedFalse(nationalCode);
    }

    public Profile saveProfile(Profile profile) {
        return profileRepository.save(profile);
    }



}