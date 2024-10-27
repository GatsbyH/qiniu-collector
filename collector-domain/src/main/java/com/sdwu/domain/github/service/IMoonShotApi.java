package com.sdwu.domain.github.service;

import java.util.List;

public interface IMoonShotApi {
    String getCountry(String location);

    String getCountryByUserRelations(List<String> followersLocations);
}
