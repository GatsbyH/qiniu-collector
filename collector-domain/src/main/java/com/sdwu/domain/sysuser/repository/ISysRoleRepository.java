package com.sdwu.domain.sysuser.repository;

import java.util.List;

public interface ISysRoleRepository {

    List<String> findRoleListByUserId(Long userId);


}
