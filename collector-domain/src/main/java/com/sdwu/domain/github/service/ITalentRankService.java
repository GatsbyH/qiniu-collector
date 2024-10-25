package com.sdwu.domain.github.service;

import java.io.IOException;
import java.util.List;

public interface ITalentRankService {
    double getTalentRankByUserName(String username) throws IOException;
}
