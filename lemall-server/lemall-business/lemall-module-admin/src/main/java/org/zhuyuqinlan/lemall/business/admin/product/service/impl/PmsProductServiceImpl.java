package org.zhuyuqinlan.lemall.business.admin.product.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.zhuyuqinlan.lemall.common.entity.*;
import org.zhuyuqinlan.lemall.business.admin.content.service.CmsPrefrenceAreaProductRelationService;
import org.zhuyuqinlan.lemall.business.admin.content.service.CmsSubjectProductRelationService;
import org.zhuyuqinlan.lemall.business.admin.product.dto.request.PmsProductQueryParamRequestDTO;
import org.zhuyuqinlan.lemall.business.admin.product.dto.request.PmsProductParamRequestDTO;
import org.zhuyuqinlan.lemall.business.admin.product.dto.response.PmsProductParamResultResponseDTO;
import org.zhuyuqinlan.lemall.business.admin.product.dto.response.PmsProductResponseDTO;
import org.zhuyuqinlan.lemall.business.admin.product.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.zhuyuqinlan.lemall.business.admin.product.mapper.PmsProductMapper;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.zhuyuqinlan.lemall.business.admin.product.service.*;
import org.zhuyuqinlan.lemall.common.entity.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
@RequiredArgsConstructor
public class PmsProductServiceImpl extends ServiceImpl<PmsProductMapper, PmsProduct> implements PmsProductService {

    private final PmsMemberPriceService pmsMemberPriceService;
    private final PmsProductLadderService pmsProductLadderService;
    private final PmsProductFullReductionService pmsProductFullReductionService;
    private final PmsSkuStockService pmsSkuStockService;
    private final PmsProductAttributeValueService pmsProductAttributeValueService;
    private final CmsSubjectProductRelationService cmsSubjectProductRelationService;
    private final CmsPrefrenceAreaProductRelationService cmsPrefrenceAreaProductRelationService;
    private final PmsProductVertifyRecordService pmsProductVertifyRecordService;

    @Override
    public IPage<PmsProductResponseDTO> getList(PmsProductQueryParamRequestDTO productQueryParam, Integer pageSize, Integer pageNum) {
        return super.page(new Page<>(pageNum, pageSize), Wrappers.<PmsProduct>lambdaQuery()
                .eq(productQueryParam.getPublishStatus() != null,PmsProduct::getPublishStatus,productQueryParam.getPublishStatus())
                .eq(productQueryParam.getVerifyStatus() != null,PmsProduct::getVerifyStatus,productQueryParam.getVerifyStatus())
                .like(StringUtils.hasText(productQueryParam.getKeyword()),PmsProduct::getKeywords,productQueryParam.getKeyword())
                .eq(StringUtils.hasText(productQueryParam.getProductSn()),PmsProduct::getProductSn,productQueryParam.getProductSn())
                .eq(productQueryParam.getProductCategoryId() != null,PmsProduct::getProductCategoryId,productQueryParam.getProductCategoryId())
                .eq(productQueryParam.getBrandId() != null,PmsProduct::getBrandId,productQueryParam.getBrandId())
                .orderByDesc(PmsProduct::getSort)
        ).convert(e -> {
            PmsProductResponseDTO responseDTO = new PmsProductResponseDTO();
            BeanUtils.copyProperties(e, responseDTO);
            return responseDTO;
        });
    }

    @Override
    public boolean create(PmsProductParamRequestDTO requestDTO) {
        // 1. 创建商品
        requestDTO.setId(null);
        PmsProduct pmsProduct = new PmsProduct();
        BeanUtils.copyProperties(requestDTO, pmsProduct);
        super.save(pmsProduct);

        // 2.根据促销类型设置加个：会员价格，阶梯价格，满减价格
        Long id = pmsProduct.getId();
        // 会员价格
        List<PmsMemberPrice> memberPrices = requestDTO.getMemberPriceList().stream().map(e -> {
            PmsMemberPrice pmsMemberPrice = new PmsMemberPrice();
            BeanUtils.copyProperties(e, pmsMemberPrice);
            pmsMemberPrice.setProductId(id);
            return pmsMemberPrice;
        }).toList();
        pmsMemberPriceService.saveBatch(memberPrices);
        // 阶梯价格
        List<PmsProductLadder> pmsProductLadders = requestDTO.getProductLadderList().stream().map(e -> {
            PmsProductLadder productLadder = new PmsProductLadder();
            BeanUtils.copyProperties(e, productLadder);
            productLadder.setProductId(id);
            return productLadder;
        }).toList();
        pmsProductLadderService.saveBatch(pmsProductLadders);
        // 满减价格
        List<PmsProductFullReduction> pmsProductFullReductions = requestDTO.getProductFullReductionList().stream().map(e -> {
            PmsProductFullReduction pmsProductFullReduction = new PmsProductFullReduction();
            BeanUtils.copyProperties(e, pmsProductFullReduction);
            pmsProductFullReduction.setProductId(id);
            return pmsProductFullReduction;
        }).toList();
        pmsProductFullReductionService.saveBatch(pmsProductFullReductions);
        // 获取po并处理sku的编码
        List<PmsSkuStock> pmsSkuStocks = requestDTO.getSkuStockList().stream().map(e -> {
            PmsSkuStock pmsSkuStock = new PmsSkuStock();
            BeanUtils.copyProperties(e, pmsSkuStock);
            pmsSkuStock.setProductId(id);
            return pmsSkuStock;
        }).toList();
        handleSkuStockCode(pmsSkuStocks,id);
        // 添加sku库存信息
        pmsSkuStockService.saveBatch(pmsSkuStocks);
        // 添加商品参数，添加自定义商品规格
        List<PmsProductAttributeValue> pmsProductAttributeValues = requestDTO.getProductAttributeValueList().stream().map(e -> {
            PmsProductAttributeValue pmsProductAttributeValue = new PmsProductAttributeValue();
            BeanUtils.copyProperties(e, pmsProductAttributeValue);
            pmsProductAttributeValue.setProductId(id);
            return pmsProductAttributeValue;
        }).toList();
        pmsProductAttributeValueService.saveBatch(pmsProductAttributeValues);
        // 关联专题
        List<CmsSubjectProductRelation> cmsSubjectProductRelations = requestDTO.getSubjectProductRelationList().stream().map(e -> {
            CmsSubjectProductRelation cmsSubjectProductRelation = new CmsSubjectProductRelation();
            BeanUtils.copyProperties(e, cmsSubjectProductRelation);
            cmsSubjectProductRelation.setProductId(id);
            return cmsSubjectProductRelation;
        }).toList();
        cmsSubjectProductRelationService.saveBatch(cmsSubjectProductRelations);
        // 关联优选
        List<CmsPrefrenceAreaProductRelation> cmsPrefrenceAreaProductRelations = requestDTO.getPrefrenceAreaProductRelationList().stream().map(e -> {
            CmsPrefrenceAreaProductRelation cmsPrefrenceAreaProductRelation = new CmsPrefrenceAreaProductRelation();
            BeanUtils.copyProperties(e, cmsPrefrenceAreaProductRelation);
            cmsPrefrenceAreaProductRelation.setProductId(id);
            return cmsPrefrenceAreaProductRelation;
        }).toList();
        cmsPrefrenceAreaProductRelationService.saveBatch(cmsPrefrenceAreaProductRelations);
        return true;
    }

    @Override
    public PmsProductParamResultResponseDTO getUpdateInfo(Long id) {
        return baseMapper.getUpdateInfo(id);
    }

    @Override
    public boolean updateProduct(Long id, PmsProductParamRequestDTO requestDTO) {
        // 更新商品信息
        PmsProduct pmsProduct = new PmsProduct();
        BeanUtils.copyProperties(requestDTO, pmsProduct);
        pmsProduct.setId(id);
        super.updateById(pmsProduct);

        //会员价格
        pmsMemberPriceService.remove(Wrappers.<PmsMemberPrice>lambdaQuery()
                .eq(PmsMemberPrice::getProductId,id)); // 先删除原来的
        List<PmsMemberPrice> pmsMemberPrices = requestDTO.getMemberPriceList().stream().map(e -> {
            PmsMemberPrice pmsMemberPrice = new PmsMemberPrice();
            BeanUtils.copyProperties(e, pmsMemberPrice);
            pmsMemberPrice.setProductId(id);
            return pmsMemberPrice;
        }).toList();
        pmsMemberPriceService.saveBatch(pmsMemberPrices); // 再添加
        //阶梯价格
        pmsProductLadderService.remove(Wrappers.<PmsProductLadder>lambdaQuery()
        .eq(PmsProductLadder::getProductId,id)); // 先删除原来的
        List<PmsProductLadder> productLadders = requestDTO.getProductLadderList().stream().map(e -> {
            PmsProductLadder productLadder = new PmsProductLadder();
            BeanUtils.copyProperties(e, productLadder);
            productLadder.setProductId(id);
            return productLadder;
        }).toList();
        pmsProductLadderService.saveBatch(productLadders); // 再添加
        //满减价格
        pmsProductFullReductionService.remove(Wrappers.<PmsProductFullReduction>lambdaQuery()
        .eq(PmsProductFullReduction::getProductId,id));
        List<PmsProductFullReduction> pmsProductFullReductions = requestDTO.getProductFullReductionList().stream().map(e -> {
            PmsProductFullReduction pmsProductFullReduction = new PmsProductFullReduction();
            BeanUtils.copyProperties(e, pmsProductFullReduction);
            pmsProductFullReduction.setProductId(id);
            return pmsProductFullReduction;
        }).toList();
        pmsProductFullReductionService.saveBatch(pmsProductFullReductions); // 再添加
        //修改sku库存信息
        pmsSkuStockService.remove(Wrappers.<PmsSkuStock>lambdaQuery()
        .eq(PmsSkuStock::getProductId,id));
        List<PmsSkuStock> pmsSkuStocks = requestDTO.getSkuStockList().stream().map(e -> {
            PmsSkuStock pmsSkuStock = new PmsSkuStock();
            BeanUtils.copyProperties(e, pmsSkuStock);
            pmsSkuStock.setProductId(id);
            return pmsSkuStock;
        }).toList();
        pmsSkuStockService.saveBatch(pmsSkuStocks);
        //修改商品参数,添加自定义商品规格
        pmsProductAttributeValueService.remove(Wrappers.<PmsProductAttributeValue>lambdaQuery()
        .eq(PmsProductAttributeValue::getProductId,id));
        List<PmsProductAttributeValue> pmsProductAttributeValues = requestDTO.getProductAttributeValueList().stream().map(e -> {
            PmsProductAttributeValue pmsProductAttributeValue = new PmsProductAttributeValue();
            BeanUtils.copyProperties(e, pmsProductAttributeValue);
            pmsProductAttributeValue.setProductId(id);
            return pmsProductAttributeValue;
        }).toList();
        pmsProductAttributeValueService.saveBatch(pmsProductAttributeValues); // 再添加
        //关联专题
        cmsSubjectProductRelationService.remove(Wrappers.<CmsSubjectProductRelation>lambdaQuery()
        .eq(CmsSubjectProductRelation::getProductId,id));
        List<CmsSubjectProductRelation> cmsSubjectProductRelations = requestDTO.getSubjectProductRelationList().stream().map(e -> {
            CmsSubjectProductRelation cmsSubjectProductRelation = new CmsSubjectProductRelation();
            BeanUtils.copyProperties(e, cmsSubjectProductRelation);
            cmsSubjectProductRelation.setProductId(id);
            return cmsSubjectProductRelation;
        }).toList();
        cmsSubjectProductRelationService.saveBatch(cmsSubjectProductRelations); // 再添加
        //关联优选
        cmsPrefrenceAreaProductRelationService.remove(Wrappers.<CmsPrefrenceAreaProductRelation>lambdaQuery()
                .eq(CmsPrefrenceAreaProductRelation::getProductId,id)); // 删除原来的
        List<CmsPrefrenceAreaProductRelation> cmsPrefrenceAreaProductRelations = requestDTO.getPrefrenceAreaProductRelationList().stream().map(e -> {
            CmsPrefrenceAreaProductRelation cmsPrefrenceAreaProductRelation = new CmsPrefrenceAreaProductRelation();
            BeanUtils.copyProperties(e, cmsPrefrenceAreaProductRelation);
            cmsPrefrenceAreaProductRelation.setProductId(id);
            return cmsPrefrenceAreaProductRelation;
        }).toList();
        cmsPrefrenceAreaProductRelationService.saveBatch(cmsPrefrenceAreaProductRelations); // 再添加
        return true;
    }

    @Override
    public List<PmsProductResponseDTO> getSimpleList(String keyword) {
       return super.list(Wrappers.<PmsProduct>lambdaQuery()
                .eq(PmsProduct::getDeleteStatus,0)
                .like(StringUtils.hasText(keyword),PmsProduct::getName,keyword)
                .like(StringUtils.hasText(keyword),PmsProduct::getProductSn,keyword)
        ).stream().map(e -> {
            PmsProductResponseDTO pmsProductResponseDTO = new PmsProductResponseDTO();
            BeanUtils.copyProperties(e, pmsProductResponseDTO);
            return pmsProductResponseDTO;
       }).toList();
    }

    @Override
    public boolean updateVerifyStatus(List<Long> ids, Integer verifyStatus, String detail) {
        super.update(Wrappers.<PmsProduct>lambdaUpdate()
                .in(PmsProduct::getId,ids)
                .set(PmsProduct::getVerifyStatus,verifyStatus)
        );
        //修改完审核状态后插入审核记录
        List<PmsProductVertifyRecord> list = new ArrayList<>();
        for (Long id : ids) {
            PmsProductVertifyRecord record = new PmsProductVertifyRecord();
            record.setProductId(id);
            record.setDetail(detail);
            record.setStatus(verifyStatus);
            record.setVertifyMan(StpUtil.getLoginId().toString());
            list.add(record);
        }
        return pmsProductVertifyRecordService.saveBatch(list);
    }

    @Override
    public boolean updatePublishStatus(List<Long> ids, Integer publishStatus) {
        return super.update(Wrappers.<PmsProduct>lambdaUpdate()
        .in(PmsProduct::getId,ids).set(PmsProduct::getPublishStatus,publishStatus));
    }

    @Override
    public boolean updateRecommendStatus(List<Long> ids, Integer recommendStatus) {
        return super.update(Wrappers.<PmsProduct>lambdaUpdate()
        .in(PmsProduct::getId,ids).set(PmsProduct::getRecommandStatus,recommendStatus));
    }

    @Override
    public boolean updateNewStatus(List<Long> ids, Integer newStatus) {
        return super.update(Wrappers.<PmsProduct>lambdaUpdate()
                .in(PmsProduct::getId,ids).set(PmsProduct::getNewStatus,newStatus));
    }

    @Override
    public boolean updateDeleteStatus(List<Long> ids, Integer deleteStatus) {
        return super.update(Wrappers.<PmsProduct>lambdaUpdate()
                .in(PmsProduct::getId,ids).set(PmsProduct::getDeleteStatus,deleteStatus));
    }

    private void handleSkuStockCode(List<PmsSkuStock> skuStockList, Long productId) {
        if(CollectionUtils.isEmpty(skuStockList))return;
        for(int i=0;i<skuStockList.size();i++){
            PmsSkuStock skuStock = skuStockList.get(i);
            if(!StringUtils.hasText(skuStock.getSkuCode())){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                //日期
                String sb = sdf.format(new Date()) +
                        //四位商品id
                        String.format("%04d", productId) +
                        //三位索引id
                        String.format("%03d", i + 1);
                skuStock.setSkuCode(sb);
            }
        }
    }
}
