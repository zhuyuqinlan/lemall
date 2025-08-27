package org.nanguo.lemall.business.admin.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nanguo.lemall.business.admin.system.dto.request.UmsMenuRequestDTO;
import org.nanguo.lemall.business.admin.system.dto.response.UmsMenuNodeResponseDTO;
import org.nanguo.lemall.business.admin.system.dto.response.UmsMenuResponseDTO;
import org.nanguo.lemall.common.entity.UmsMenu;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UmsMenuService extends IService<UmsMenu>{

    /**
     * 树结构返回菜单
     * @return 结果
     */
    List<UmsMenuNodeResponseDTO> treeList();

    /**
     * 分页查询菜单
     * @param parentId 父节点id
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return 分页结果
     */
    IPage<UmsMenuResponseDTO> pageMenu(Long parentId, Integer pageNum, Integer pageSize);

    /**
     * 创建菜单
     * @param umsMenuParamRequestDTO 请求参数
     * @return 受影响的行数
     */
    int create(UmsMenuRequestDTO umsMenuParamRequestDTO);

    /**
     * 根据id获取菜单
     * @param id id
     * @return dto
     */
    UmsMenuResponseDTO getItem(Long id);

    /**
     * 修改后台菜单
     * @param umsMenuParamRequestDTO 数据
     * @param id id
     * @return 成功标志
     */
    boolean updateMenu(UmsMenuRequestDTO umsMenuParamRequestDTO, Long id);

    /**
     * 删除后台菜单
     * @param id id
     * @return 成功标志
     */
    @Transactional(rollbackFor = Exception.class)
    boolean deleteMenu(Long id);
}
