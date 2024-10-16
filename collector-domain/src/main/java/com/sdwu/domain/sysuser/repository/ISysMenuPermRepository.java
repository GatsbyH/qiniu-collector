package com.sdwu.domain.sysuser.repository;

import java.util.List;

public interface ISysMenuPermRepository {
    List<String> findMenuPermListByUserId(Long userId);

}
