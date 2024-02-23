package mapper;

import model.Profile;
import org.mapstruct.Mapper;
import service.profile.model.ProfileRequest;

@Mapper(componentModel = "spring")
public interface ProfileMapper {

    Profile toProfile(ProfileRequest profileRequest);
}
