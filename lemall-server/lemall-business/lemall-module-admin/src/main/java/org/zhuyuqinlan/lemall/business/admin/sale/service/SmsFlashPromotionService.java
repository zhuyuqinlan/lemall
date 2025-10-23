package org.zhuyuqinlan.lemall.business.admin.sale.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.zhuyuqinlan.lemall.common.entity.SmsFlashPromotion;
import org.zhuyuqinlan.lemall.common.mapper.SmsFlashPromotionMapper;
import org.zhuyuqinlan.lemall.business.admin.sale.dto.request.SmsFlashPromotionRequestDTO;
import org.zhuyuqinlan.lemall.business.admin.sale.dto.SmsFlashPromotionDTO;

/**
 * 秒杀活动管理Service
 */
@Service
public class SmsFlashPromotionService extends ServiceImpl<SmsFlashPromotionMapper, SmsFlashPromotion> {

    /**
     * 添加活动
     * @param flashPromotion 请求参数
     * @return 成功标志
     */
    public boolean create(SmsFlashPromotionRequestDTO flashPromotion) {
        SmsFlashPromotion smsFlashPromotion = new SmsFlashPromotion();
        BeanUtils.copyProperties(flashPromotion, smsFlashPromotion);
        return save(smsFlashPromotion);
    }

    /**
     * 编辑活动信息
     * @param id id
     * @param flashPromotion 请求参数
     * @return 成功标志
     */
    public boolean updateFlash(Long id, SmsFlashPromotionRequestDTO flashPromotion) {
        SmsFlashPromotion smsFlashPromotion = new SmsFlashPromotion();
        BeanUtils.copyProperties(flashPromotion, smsFlashPromotion);
        smsFlashPromotion.setId(id);
        return updateById(smsFlashPromotion);
    }

    /**
     * 删除活动信息
     * @param id id
     * @return 成功标志
     */
    public boolean delete(Long id) {
        return removeById(id);
    }

    /**
     * 修改上下线状态
     * @param id id
     * @param status 状态
     * @return 成功标志
     */
    public boolean updateStatus(Long id, Integer status) {
        return update(Wrappers.<SmsFlashPromotion>lambdaUpdate()
                .eq(SmsFlashPromotion::getId, id)
                .set(SmsFlashPromotion::getStatus, status)
        );
    }

    /**
     * 获取活动详情
     * @param id id
     * @return 结果
     */
    public SmsFlashPromotionDTO getItem(Long id) {
        SmsFlashPromotion smsFlashPromotion = getById(id);
        SmsFlashPromotionDTO responseDTO = new SmsFlashPromotionDTO();
        BeanUtils.copyProperties(smsFlashPromotion, responseDTO);
        return responseDTO;
    }

    /**
     * 根据活动名称分页查询
     * @param keyword 关键词
     * @param pageSize 每页条数
     * @param pageNum 页码
     * @return 结果
     */
    public IPage<SmsFlashPromotionDTO> listPage(String keyword, Integer pageSize, Integer pageNum) {
        return page(new Page<>(pageNum, pageSize), Wrappers.<SmsFlashPromotion>lambdaQuery()
                .like(StringUtils.hasText(keyword), SmsFlashPromotion::getTitle, keyword)
        ).convert(e -> {
            SmsFlashPromotionDTO responseDTO = new SmsFlashPromotionDTO();
            BeanUtils.copyProperties(e, responseDTO);
            return responseDTO;
        });
    }
}
