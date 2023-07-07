package andrey.library.booking.mapper;

import andrey.library.booking.dto.ClientDto;
import andrey.library.booking.model.Client;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ClientMapper {

    @Mapping(target = "account", source = "clientAccount")
    Client toClient(String clientAccount);

    @InheritInverseConfiguration
    ClientDto toClientDto(Client client);
}
