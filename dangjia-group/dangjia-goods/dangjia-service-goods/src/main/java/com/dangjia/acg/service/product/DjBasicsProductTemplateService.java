package com.dangjia.acg.service.product;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dangjia.acg.api.app.repair.MasterMendWorkerAPI;
import com.dangjia.acg.common.constants.SysConfig;
import com.dangjia.acg.common.exception.ServerCode;
import com.dangjia.acg.common.model.PageDTO;
import com.dangjia.acg.common.response.ServerResponse;
import com.dangjia.acg.common.util.BeanUtils;
import com.dangjia.acg.common.util.CommonUtil;
import com.dangjia.acg.dao.ConfigUtil;
import com.dangjia.acg.dto.product.ActuarialGoodsDTO;
import com.dangjia.acg.dto.product.BasicsProductDTO;
import com.dangjia.acg.dto.product.DjBasicsLabelDTO;
import com.dangjia.acg.dto.product.DjBasicsProductLabelDTO;
import com.dangjia.acg.mapper.actuary.IBudgetWorkerMapper;
import com.dangjia.acg.mapper.basics.IAttributeValueMapper;
import com.dangjia.acg.mapper.basics.ILabelMapper;
import com.dangjia.acg.mapper.basics.ITechnologyMapper;
import com.dangjia.acg.mapper.basics.IUnitMapper;
import com.dangjia.acg.mapper.product.*;
import com.dangjia.acg.modle.attribute.AttributeValue;
import com.dangjia.acg.modle.basics.Label;
import com.dangjia.acg.modle.basics.Technology;
import com.dangjia.acg.modle.brand.Unit;
import com.dangjia.acg.modle.product.*;
import com.dangjia.acg.service.basics.TechnologyService;
import com.dangjia.acg.util.StringTool;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/**
 * 产品逻辑处理层
 * author: wk
 * Date: 2019/7/25
 * Time: 13:56
 */
@Service
public class DjBasicsProductTemplateService {
    private static Logger LOG = LoggerFactory.getLogger(DjBasicsProductTemplateService.class);
    @Autowired
    private IBasicsProductTemplateMapper iBasicsProductTemplateMapper;
    @Autowired
    private DjBasicsLabelMapper djBasicsLabelMapper;
    @Autowired
    private DjBasicsLabelValueMapper djBasicsLabelValueMapper;
    @Autowired
    private DjBasicsProductLabelValMapper djBasicsProductLabelValMapper;

    @Autowired
    private IBasicsGoodsMapper iBasicsGoodsMapper;
    @Autowired
    private TechnologyService technologyService;

    @Autowired
    private ITechnologyMapper iTechnologyMapper;

    @Autowired
    private DjBasicsGoodsMapper djBasicsGoodsMapper;

    @Autowired
    private IBudgetWorkerMapper iBudgetWorkerMapper;
    @Autowired
    private MasterMendWorkerAPI masterMendWorkerAPI;
    @Autowired
    private IUnitMapper iUnitMapper;
    @Autowired
    private ConfigUtil configUtil;
    @Autowired
    private IAttributeValueMapper iAttributeValueMapper;
    @Autowired
    private ILabelMapper iLabelMapper;
    /**
     * 查询商品信息
     *
     * @param name
     * @return
     */
    public ServerResponse queryProductData(String name) {
        Example example = new Example(DjBasicsProduct.class);
        if (!CommonUtil.isEmpty(name)) {
            example.createCriteria().andLike(DjBasicsProduct.NAME, "%" + name + "%");
            List<DjBasicsProductTemplate> list = iBasicsProductTemplateMapper.selectByExample(example);
            if (list.size() <= 0) {
                return ServerResponse.createByErrorCodeMessage(ServerCode.NO_DATA.getCode(), ServerCode.NO_DATA.getDesc());
            }
            return ServerResponse.createBySuccess("查询成功", list);
        }
        return ServerResponse.createByErrorCodeMessage(ServerCode.NO_DATA.getCode(), ServerCode.NO_DATA.getDesc());
    }

    /**
     * 根据类型查询同级货品
     * @param categoryId
     * @return
     */
    public List<DjBasicsProductTemplate> getAllProductByCategoryId(String categoryId) {
        try {
            Example example = new Example(DjBasicsProduct.class);
            example.createCriteria().andEqualTo(DjBasicsProduct.CATEGORY_ID,categoryId);
            List<DjBasicsProductTemplate> djBasicsProductList = iBasicsProductTemplateMapper.selectByExample(example); //根据商品编号查询对象
            return djBasicsProductList;
        } catch (Exception e) {
            return null;
        }
    }
    /**
     * 查看商品详情
     * @param productId
     * @return
     */
    public DjBasicsProductTemplate queryDataByProductId(String productId) {
        try {
            Example example = new Example(DjBasicsProduct.class);
            example.createCriteria().andEqualTo(DjBasicsProduct.ID,productId);
            DjBasicsProductTemplate djBasicsProduct = iBasicsProductTemplateMapper.selectByPrimaryKey(example); //根据商品编号查询对象
            return djBasicsProduct;
        } catch (Exception e) {
            return null;
        }
    }



    /**
     * 保存product
     * 0.判断商品是否重复
     * 1.保存商品共有信息
     * 2.保存人工或材料商品的个性化信息
     * <p>Title: insertProduct</p>
     * <p>Description: </p>
     *
     * @param productArr
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ServerResponse insertBatchProduct(String productArr) {

        JSONArray jsonArr = JSONArray.parseArray(productArr);
        //1.商品作校验，校验前端传过来的商品是否符合条件
        String resCheckStr = checkProductData(jsonArr);
        if(StringUtils.isNotBlank(resCheckStr)){
            return ServerResponse.createByErrorMessage(resCheckStr);
        }

        //2.添加商品信息
        for (int i = 0; i < jsonArr.size(); i++) {
            JSONObject obj = jsonArr.getJSONObject(i);
            BasicsProductDTO basicsProductDTO = JSONObject.toJavaObject(obj, BasicsProductDTO.class);
            String goodsId=basicsProductDTO.getGoodsId();//货品ID
            DjBasicsGoods basicsGoods = djBasicsGoodsMapper.selectByPrimaryKey(goodsId);//查询货品表信息，判断是人工还是材料商品新增
            //2.1添加商品主表信息
            String[] imgArr = basicsProductDTO.getImage().split(",");
//                String[] technologyIds = obj.getString("technologyIds").split(",");//工艺节点
            StringBuilder imgStr = new StringBuilder();
            for (int j = 0; j < imgArr.length; j++) {
                String img = imgArr[j];
                if (j == imgArr.length - 1) {
                    imgStr.append(img);
                } else {
                    imgStr.append(img).append(",");
                }
            }
            if (!StringUtils.isNotBlank(imgStr.toString()))
                return ServerResponse.createByErrorMessage("商品图片不能为空");
            LOG.info("001----------添加商品主表 start:" + basicsProductDTO.getName());
            String productId = insertBasicsProductData(basicsProductDTO,imgStr,0,basicsGoods.getType());
            LOG.info("001----------添加商品主表 end productId:" + productId);

            //3.删除对应需要删除的工艺信息
            String deleteTechnologyIds=obj.getString("deleteTechnologyIds");
            String restr = deleteTechnologylist(deleteTechnologyIds);
            if (StringUtils.isNotBlank(restr)) {
                return ServerResponse.createByErrorMessage(restr);
            }
        }
        return ServerResponse.createBySuccessMessage("保存更新商品成功");
    }

    /**
     * 删除工艺信息
     * @param deleteTechnologyIds
     * @return
     */
    private String deleteTechnologylist(String deleteTechnologyIds){
        if (!CommonUtil.isEmpty(deleteTechnologyIds)) {
            String[] deleteTechnologyIdArr = deleteTechnologyIds.split(",");
            for (String aDeleteTechnologyIdArr : deleteTechnologyIdArr) {
                if (iTechnologyMapper.selectByPrimaryKey(aDeleteTechnologyIdArr) != null) {
                    if (iTechnologyMapper.deleteByPrimaryKey(aDeleteTechnologyIdArr) < 0)
                        return "删除id：" + aDeleteTechnologyIdArr + "失败";
                }
            }
        }
        return "";
    }

    /**
     * 添加商品主表信息
     * @param basicsProductDTO
     * @param imgStr 图处地址
     * @returndataStatus  数据状态，0正常，1删除，2存草稿
     */
    private String insertBasicsProductData(BasicsProductDTO basicsProductDTO,StringBuilder imgStr,int dataStatus,int productType){
        DjBasicsProductTemplate product = new DjBasicsProductTemplate();
        String productId = basicsProductDTO.getId();
        product.setName(basicsProductDTO.getName());//product品名称
        product.setCategoryId(basicsProductDTO.getCategoryId());//分类id
        product.setGoodsId(basicsProductDTO.getGoodsId());//goodsid
        String productSn = basicsProductDTO.getProductSn();
        product.setProductSn(productSn);//商品编号
        product.setImage(imgStr.toString());//图片地址
        product.setUnitId(basicsProductDTO.getUnitId());//单位
        product.setUnitName(basicsProductDTO.getUnitName());//单位
        product.setType(basicsProductDTO.getType()==null?1:basicsProductDTO.getType());//是否禁用0：禁用；1不禁用
        product.setMaket(basicsProductDTO.getMaket()==null?1:basicsProductDTO.getMaket());//是否上架0：不上架；1：上架
        product.setPrice(basicsProductDTO.getPrice());//销售价
        product.setOtherName(basicsProductDTO.getOtherName());
        if (!StringUtils.isNoneBlank(basicsProductDTO.getValueNameArr())) {
            product.setValueNameArr(null);
        } else {
            product.setValueNameArr(basicsProductDTO.getValueNameArr());
        }
        if (!StringUtils.isNoneBlank(basicsProductDTO.getValueIdArr())) {
            product.setValueIdArr(null);
        } else {
            product.setValueIdArr(basicsProductDTO.getValueIdArr());
        }

        product.setDataStatus(dataStatus);
        product.setWorkExplain(basicsProductDTO.getWorkExplain());
        product.setWorkerDec(basicsProductDTO.getWorkerDec());
        product.setWorkerStandard(basicsProductDTO.getWorkerStandard());
        product.setWorkerTypeId(basicsProductDTO.getWorkerTypeId());
        product.setLastPrice(basicsProductDTO.getLastPrice());
        product.setLastTime(basicsProductDTO.getLastTime());
        product.setTechnologyIds(basicsProductDTO.getTechnologyIds());
        product.setConsiderations(basicsProductDTO.getConsiderations());
        product.setCalculateContent(basicsProductDTO.getCalculateContent());
        product.setBuildContent(basicsProductDTO.getBuildContent());
        product.setIsAgencyPurchase(basicsProductDTO.getIsAgencyPurchase());

        product.setWeight(basicsProductDTO.getWeight());
        product.setCost(basicsProductDTO.getCost());
        product.setProfit(basicsProductDTO.getProfit());
        product.setConvertQuality(basicsProductDTO.getConvertQuality());
        product.setConvertUnit(basicsProductDTO.getConvertUnit());
        product.setIsInflueWarrantyPeriod(basicsProductDTO.getIsInflueWarrantyPeriod());
        product.setWorkerTypeId(basicsProductDTO.getWorkerTypeId());
        product.setMaxWarrantyPeriodYear(basicsProductDTO.getMaxWarrantyPeriodYear());
        product.setMinWarrantyPeriodYear(basicsProductDTO.getMinWarrantyPeriodYear());
        product.setMarketingName(basicsProductDTO.getMarketingName());
        product.setCartagePrice(basicsProductDTO.getCartagePrice());
        product.setDetailImage(basicsProductDTO.getDetailImage());
        product.setGuaranteedPolicy(basicsProductDTO.getGuaranteedPolicy());
        product.setRefundPolicy(basicsProductDTO.getRefundPolicy());

        if (productId == null || "".equals(productId)) {//没有id则新增
            product.setCreateDate(new Date());
            product.setModifyDate(new Date());
            iBasicsProductTemplateMapper.insert(product);

        } else {//修改
            product.setId(productId);
            product.setModifyDate(new Date());
            int  index=iBasicsProductTemplateMapper.updateByPrimaryKeySelective(product);

            if (index < 0) {
                return "更新商品失败";
            } else if(productType==2){//如果是人工商品，则作相应更新
                //相关联表也更新
                iBudgetWorkerMapper.updateBudgetMaterialByProductId(productId);
                Example example = new Example(DjBasicsProductTemplate.class);
                example.createCriteria().andEqualTo(DjBasicsProductTemplate.ID, productId);
                List<DjBasicsProductTemplate> list = iBasicsProductTemplateMapper.selectByExample(example);
                masterMendWorkerAPI.updateMendWorker(JSON.toJSONString(list));
            }
        }
        return product.getId();
    }

    /**
     * 校验需添加商品数据是否正确
     * 1.校验字段是否为空
     * 2.校验材料商品的属性字段
     * 3校验商品是否已存在
     * @param jsonArr
     * @return
     */
    private String checkProductData(JSONArray jsonArr){
        for (int i = 0; i < jsonArr.size(); i++) {
            JSONObject obj = jsonArr.getJSONObject(i);
            //JSON对象转换成Java对象
            BasicsProductDTO basicsProductDTO = JSONObject.toJavaObject(obj, BasicsProductDTO.class);
            //1.判断必填字段是否为空
            String checkStr = checkFielsNull(basicsProductDTO);
            if(StringUtils.isNotBlank(checkStr)){
                return checkStr;
            }
            String id = basicsProductDTO.getId();//商品ID
            String name = basicsProductDTO.getName();//商品名称
            String productSn = basicsProductDTO.getProductSn();//商品编码
            //改版后，人工和材料都需要添加属性字段，故去掉判断
            //String goodsId=basicsProductDTO.getGoodsId();//货品ID
            //DjBasicsGoods basicsGoods = djBasicsGoodsMapper.selectByPrimaryKey(goodsId);
            //if("0".equals(basicsGoods.getType())||"1".equals(basicsGoods.getType())){
            //判断当前添加的属性值是否有相同的已存在的商品（材料商品才有）
            checkStr = checkProductAttr(basicsProductDTO,jsonArr);
            if(StringUtils.isNotBlank(checkStr)){
                return checkStr;
            }
            //  }
            //校验商品是否存在
            String ret = checkProduct(name, productSn, id, jsonArr);
            if (!ret.equals("ok")) {
                return ret;
            }
        }
        return "";
    }
    /**
     * 判断校验字段是否可以为空
     * @param basicsProductDTO
     * @return  //
     */
    private String checkFielsNull(BasicsProductDTO basicsProductDTO){
        //判断添加商品时，对应的字段不能为空
        if (!StringUtils.isNotBlank(basicsProductDTO.getCategoryId()))
            return "商品分类不能为空";

        if (!StringUtils.isNotBlank(basicsProductDTO.getGoodsId()))
            return "货品id不能为空";

        if (!StringUtils.isNotBlank(basicsProductDTO.getProductSn()))//商品编号
            return "商品编号不能为空";

        if (!StringUtils.isNotBlank(basicsProductDTO.getName()))//商品名字
            return "商品名字不能为空";

      /*  Double convertQuality = basicsProductDTO;//换算量
        LOG.info("insertProduct convertQuality:" + convertQuality);
        if (convertQuality <= 0)
            return "换算量必须大于0";
*/
        return "";
    }

    /**
     * 判断属性值添加是否重复
     * @param basicsProductDTO
     * @param jsonArr
     * @return
     */
    public String checkProductAttr(BasicsProductDTO basicsProductDTO,JSONArray jsonArr){
        if (!StringUtils.isNotBlank(basicsProductDTO.getId())) {//没有id则新增,判断是当前添加的属性值是否重复，是否已存在
            int valueIdArrCount = 0;
            String valueIdArr = basicsProductDTO.getValueIdArr();
            String productSn = basicsProductDTO.getProductSn();
            String id=basicsProductDTO.getId();
            String name=basicsProductDTO.getName();
            //属性值判断
            if (StringUtils.isNoneBlank(valueIdArr)) {

                List<DjBasicsProductTemplate> pValueList = iBasicsProductTemplateMapper.getPListByValueIdArr(valueIdArr);
                if (pValueList.size() > 0) {
                    String ret = checkProduct(name, productSn, id, jsonArr);
                    if (!ret.equals("ok")) {
                        return "属性值已存在,请检查编号:" + productSn;
                    }
                }

                //统计添加时是否存在同属性的
                for (int j = 0; j < jsonArr.size(); j++) {
                    JSONObject objJ = jsonArr.getJSONObject(j);
                    if (valueIdArr.equals(objJ.getString("valueIdArr"))) {
                        valueIdArrCount++;
                        if (valueIdArrCount > 1) {
                            String ret = checkProduct(name, productSn, id, jsonArr);
                            if (!ret.equals("ok")) {
                                return "属性值不能重复,请检查编号:" + productSn;
//                                        return ServerResponse.createByErrorMessage("无品牌无系列属性值不能重复,商品编号“" + objJ.getString("productSn") + "”");
                            }
                        }
                    }
                }
            }
        }
        return "";
    }


    /**
     * 检商商品是否添加重复
     * @param name
     * @param productSn
     * @param id
     * @param jsonArr
     * @return
     */
    public String checkProduct(String name, String productSn, String id, JSONArray jsonArr) {
        List<DjBasicsProductTemplate> nameList = iBasicsProductTemplateMapper.queryByName(name);
        List<DjBasicsProductTemplate> productSnList = iBasicsProductTemplateMapper.queryByProductSn(productSn);
        if (!StringUtils.isNotBlank(id)) {//没有id则新增
            if (nameList.size() > 0)
                return "名字“" + nameList.get(0).getName() + "”已存在";
            if (productSnList.size() > 0)
                return "编号“:" + productSnList.get(0).getProductSn() + "”已存在";
            int snCount = 0;
            int nameCount = 0;
            for (int j = 0; j < jsonArr.size(); j++) {
                JSONObject objJ = jsonArr.getJSONObject(j);
                if (productSn.equals(objJ.getString("productSn"))) {
                    snCount++;
                    if (snCount > 1)
                        return "编号“" + productSn + "”不能重复";
                }
                if (name.equals(objJ.getString("name"))) {
                    nameCount++;
                    if (nameCount > 1)
                        return "名字“" + name + "”不能重复";
                }
            }
        } else {//修改
            DjBasicsProductTemplate oldProduct = iBasicsProductTemplateMapper.selectByPrimaryKey(id);
            if (!oldProduct.getName().equals(name)) {
                if (nameList.size() > 0)
                    return "名字“" + name + "”已存在";
            }
            if (!oldProduct.getProductSn().equals(productSn)) {
                if (productSnList.size() > 0)
                    return "编号“" + productSn + "”已存在";
            }
        }

        return "ok";
    }

    /**
     * 校验商品是否符合添加条件
     * @param basicsProductDTO
     * @param type 0材料，1包工包料，2人工
     * @return
     */
    public String checkSingleProductCommon(BasicsProductDTO basicsProductDTO,int type,JSONArray jsonArr){
        //1.判断必填字段是否为空
        String checkStr = checkFielsNull(basicsProductDTO);
        if(StringUtils.isNotBlank(checkStr)){
            return checkStr;
        }
        String id = basicsProductDTO.getId();//商品ID
        String name = basicsProductDTO.getName();//商品名称
        String productSn = basicsProductDTO.getProductSn();//商品编码
        String categoryId=basicsProductDTO.getCategoryId();//商品类别Id
        DjBasicsGoods basicsGoods = djBasicsGoodsMapper.selectByPrimaryKey(categoryId);
        if(type == 0 || type == 1){
            //判断当前添加的属性值是否有相同的已存在的商品（材料商品才有）
            checkStr = checkProductAttr(basicsProductDTO,jsonArr);
            if(StringUtils.isNotBlank(checkStr)){
                return checkStr;
            }
        }
        //校验商品是否存在
        String ret = checkProduct(name, productSn, id, jsonArr);
        if (!ret.equals("ok")) {
            return ret;
        }
        return "";
    }
    /**
     * 商品信息暂存
     *
     * @param basicsProductDTO
     * @param technologyList
     * @param deleteTechnologyIds
     * @param dataStatus 数据状态，0正常，1暂存
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ServerResponse saveProductTemporaryStorage(BasicsProductDTO basicsProductDTO,String technologyList, String  deleteTechnologyIds,int dataStatus){
        if (!StringUtils.isNotBlank(basicsProductDTO.getCategoryId()))
            return ServerResponse.createByErrorMessage("商品分类不能为空");

        if (!StringUtils.isNotBlank(basicsProductDTO.getGoodsId()))
            return ServerResponse.createByErrorMessage("货品id不能为空");
        String goodsId=basicsProductDTO.getGoodsId();//货品ID
        DjBasicsGoods basicsGoods = djBasicsGoodsMapper.selectByPrimaryKey(goodsId);//查询货品表信息，判断是人工还是材料商品新增
        if(dataStatus == 0){
            //添加正式商品前的校验，商品名称和编码不能为空，且不能重复
            String restr = checkSingleProductCommon(basicsProductDTO,basicsGoods.getType(),new JSONArray());
            if(StringUtils.isNotBlank(restr)){
                return ServerResponse.createByErrorMessage(restr);
            }
        }
        //2.1添加商品主表信息
        StringBuilder imgStr = new StringBuilder();
        if(StringUtils.isNotBlank(basicsProductDTO.getImage())){
            String[] imgArr = basicsProductDTO.getImage().split(",");
//                String[] technologyIds = obj.getString("technologyIds").split(",");//工艺节点
            for (int j = 0; j < imgArr.length; j++) {
                String img = imgArr[j];
                if (j == imgArr.length - 1) {
                    imgStr.append(img);
                } else {
                    imgStr.append(img).append(",");
                }
            }
        }
        LOG.info("001----------添加商品主表 start:" +basicsProductDTO.getId()+"-----"+ basicsProductDTO.getName());
        String productId = insertBasicsProductData(basicsProductDTO,imgStr,dataStatus,basicsGoods.getType());
        LOG.info("001----------添加商品主表 end productId:" + productId);
        //3.删除对应需要删除的工艺信息
        String restr = deleteTechnologylist(deleteTechnologyIds);
        if (StringUtils.isNotBlank(restr)) {
            return ServerResponse.createByErrorMessage(restr);
        }
        return ServerResponse.createBySuccess("保存成功",productId);
    }
    /**
     * 查询商品标签
     *
     * @param productId
     * @return
     */
    public ServerResponse queryProductLabels(String productId) {
        Example example=new Example(DjBasicsProductLabelVal.class);
        example.createCriteria().andEqualTo(DjBasicsProductLabelVal.PRODUCT_ID,productId);
        List<DjBasicsProductLabelVal> djBasicsProductLabelVals = djBasicsProductLabelValMapper.selectByExample(example);
        List<DjBasicsProductLabelDTO> djBasicsProductLabelDTOS = new ArrayList<>();
        djBasicsProductLabelVals.forEach(dbpl ->{
            DjBasicsLabel djBasicsLabel = djBasicsLabelMapper.selectByPrimaryKey(dbpl.getLabelId());
            DjBasicsProductLabelDTO djBasicsProductLabelDTO = new DjBasicsProductLabelDTO();
            djBasicsProductLabelDTO.setLabelId(djBasicsLabel.getId());
            djBasicsProductLabelDTO.setLabelValId(Arrays.asList(dbpl.getLabelValId().split(",")));
            djBasicsProductLabelDTOS.add(djBasicsProductLabelDTO);
        });
        if (djBasicsProductLabelDTOS.size() <= 0)
            return ServerResponse.createByErrorCodeMessage(ServerCode.NO_DATA.getCode(), ServerCode.NO_DATA.getDesc());
        return ServerResponse.createBySuccess("查询成功", djBasicsProductLabelDTOS);
    }


    /**
     * 商品打标签
     *
     * @return
     */
    public ServerResponse addLabelsValue(String jsonStr) {
        try {
            JSONObject villageObj = JSONObject.parseObject(jsonStr);
            String productId = villageObj.getString("productId");//商品id
            //遍历标签值对象 数组  ， 一个商品 对应 多个标签
            String productLabelValList = villageObj.getString("productLabelValList");
            JSONArray productLabelValArr = JSONArray.parseArray(productLabelValList);
            for (int i = 0; i < productLabelValArr.size(); i++) {//遍历户型
                JSONObject obj = productLabelValArr.getJSONObject(i);
                String labelId = obj.getString("labelId");//标签id
                String labelValId = obj.getString("labelValId");//标签值id
                JSONArray jsonObject = JSONArray.parseArray(labelValId);
                StringBuffer stringBuffer=new StringBuffer();
                for (int i1 = 0; i1 < jsonObject.size(); i1++) {
                    if(i1!=0)
                        stringBuffer.append(",");
                    stringBuffer.append(jsonObject.get(i1));
                }
                System.out.println(stringBuffer.toString());
                DjBasicsProductLabelVal djBasicsProductLabelVal;
                Example example=new Example(DjBasicsProductLabelVal.class);
                example.createCriteria().andEqualTo(DjBasicsProductLabelVal.PRODUCT_ID,productId)
                        .andEqualTo(DjBasicsProductLabelVal.LABEL_ID,labelId);
                List<DjBasicsProductLabelVal> djBasicsProductLabelVals = djBasicsProductLabelValMapper.selectByExample(example);
                if (djBasicsProductLabelVals.size()<=0) {//没有则新增
                    djBasicsProductLabelVal = new DjBasicsProductLabelVal();
                    djBasicsProductLabelVal.setProductId(productId);
                    djBasicsProductLabelVal.setDataStatus(0);
                    djBasicsProductLabelVal.setLabelId(labelId);
                    djBasicsProductLabelVal.setLabelValId(stringBuffer.toString());
                    djBasicsProductLabelValMapper.insert(djBasicsProductLabelVal);
                } else {
                    djBasicsProductLabelVal=new DjBasicsProductLabelVal();
                    djBasicsProductLabelVal.setLabelValId(stringBuffer.toString());
                    example=new Example(DjBasicsProductLabelVal.class);
                    example.createCriteria().andEqualTo(DjBasicsProductLabelVal.PRODUCT_ID,productId)
                            .andEqualTo(DjBasicsProductLabelVal.LABEL_ID,labelId);
                    djBasicsProductLabelValMapper.updateByExampleSelective(djBasicsProductLabelVal,example);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponse.createByErrorMessage("操作失败");
        }
        return ServerResponse.createBySuccessMessage("操作成功");


    }



    /**
     * 根据productid删除product对象
     *
     * @param id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ServerResponse deleteBasicsProductById(String id) {
        DjBasicsProduct djBasicsProduct = new DjBasicsProduct();
        djBasicsProduct.setId(id);
        iBasicsProductTemplateMapper.deleteByPrimaryKey(djBasicsProduct);
        return ServerResponse.createBySuccessMessage("删除成功");
    }



    /**
     * 模糊查询goods及下属product
     *
     * @param pageDTO
     * @param categoryId
     * @param name 分类名称
     * @param type 是否禁用  0：禁用；1不禁用 ;  -1全部默认
     * @return
     */
    public ServerResponse queryGoodsListByCategoryLikeName(PageDTO pageDTO, String categoryId, String name, Integer type, String categoryName) {
        try {
            LOG.info("tqueryGoodsListByCategoryLikeName type :" + type);
            PageHelper.startPage(pageDTO.getPageNum(), pageDTO.getPageSize());
            List<DjBasicsGoods> djBasicsGoods = djBasicsGoodsMapper.queryGoodsListByCategoryLikeName(categoryId, name);
            PageInfo pageResult = new PageInfo(djBasicsGoods);
            List<ActuarialGoodsDTO> actuarialGoodsDTOS=new ArrayList<>();
            List<Map<String, Object>> gMapList = new ArrayList<>();
            ActuarialGoodsDTO actuarialGoodsDTO=new ActuarialGoodsDTO();
            actuarialGoodsDTO.setCategoryName(categoryName);
            djBasicsGoods.forEach(goods ->{
                Map<String, Object> gMap = BeanUtils.beanToMap(goods);
                List<Map<String, Object>> mapList = new ArrayList<>();
                if (2 != goods.getBuy()) {
                    List<DjBasicsProductTemplate> djBasicsProducts = iBasicsProductTemplateMapper.queryByGoodsId(goods.getId());
                    for (DjBasicsProductTemplate p : djBasicsProducts) {
                        //type表示： 是否禁用  0：禁用；1不禁用 ;  -1全部默认
                        if (type!=null&& !type.equals(p.getType()) && -1 != type) //不等于 type 的不返回给前端
                            continue;
                        Map<String, Object> map = BeanUtils.beanToMap(p);
                        mapList.add(map);
                    }
                    gMap.put("productList", mapList);
                    gMapList.add(gMap);
                }
            });
            actuarialGoodsDTO.setGMapList(gMapList);
            actuarialGoodsDTOS.add(actuarialGoodsDTO);
            pageResult.setList(actuarialGoodsDTOS);
            return ServerResponse.createBySuccess("查询成功", pageResult);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponse.createByErrorMessage("查询失败");
        }
    }





    /**
     * 查询所有的商品
     * @param pageDTO
     * @param categoryId
     * @return
     */
    public ServerResponse<PageInfo> queryProduct(PageDTO pageDTO, String categoryId) {
        try {
            PageHelper.startPage(pageDTO.getPageNum(), pageDTO.getPageSize());
            String address = configUtil.getValue(SysConfig.PUBLIC_DANGJIA_ADDRESS, String.class);
            List<DjBasicsProductTemplate> productList = iBasicsProductTemplateMapper.queryProductByCategoryId(categoryId);
            PageInfo pageResult = new PageInfo(productList);
            List<Map<String, Object>> mapList = new ArrayList<>();
            for (DjBasicsProductTemplate p : productList) {
                if (p.getImage() == null) {
                    continue;
                }
                String[] imgArr = p.getImage().split(",");
                StringBuilder imgStr = new StringBuilder();
                StringBuilder imgUrlStr = new StringBuilder();
                StringTool.getImages(address, imgArr, imgStr, imgUrlStr);
                p.setImage(imgStr.toString());
                Map<String, Object> map = BeanUtils.beanToMap(p);
                map.put("imageUrl", imgUrlStr.toString());
                mapList.add(map);
            }
            pageResult.setList(mapList);
            return ServerResponse.createBySuccess("查询成功", pageResult);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponse.createByErrorMessage("查询失败");
        }
    }

    /**
     * 查询所有单位列表
     * @return
     */
    public ServerResponse queryUnit() {
        try {
            List<Unit> unitList = iUnitMapper.getUnit();
            return ServerResponse.createBySuccess("查询成功", unitList);
        } catch (Exception e) {
            LOG.error("查询失败：",e);
            return ServerResponse.createByErrorMessage("查询失败");
        }
    }

    /**
     * 根据productid查询product对象
     *
     * @param id
     * @return
     */
    public ServerResponse getProductById(String id) {
        try {
            DjBasicsProductTemplate djBasicsProduct =iBasicsProductTemplateMapper.selectByPrimaryKey(id);
            Map<String,Object> map = null;
            if(djBasicsProduct!=null&&StringUtils.isNotBlank(djBasicsProduct.getId())){
                map = getProductDetailByProductId(djBasicsProduct);
            }
            return ServerResponse.createBySuccess("查询成功", map);
        } catch (Exception e) {
            LOG.error("查询失败：",e);
            return ServerResponse.createByErrorMessage("查询失败");
        }
    }

    /**
     * 查询货品下暂存的商品信息
     *
     * @param goodsId
     * @return
     */
    public ServerResponse getTemporaryStorageProductByGoodsId(String goodsId) {
        try {
            DjBasicsProductTemplate djBasicsProduct =iBasicsProductTemplateMapper.queryTemporaryStorage(goodsId,"2");
            Map<String,Object> map = null;
            if(djBasicsProduct!=null&&StringUtils.isNotBlank(djBasicsProduct.getId())){
                map = getProductDetailByProductId(djBasicsProduct);
            }
            return ServerResponse.createBySuccess("查询成功", map);
        } catch (Exception e) {
            LOG.error("查询失败：",e);
            return ServerResponse.createByErrorMessage("查询失败");
        }
    }


    /**
     * 根据商品ID查询对应的商品详情信息
     * 1.材料或人工扩展信息
     * 2.单位信息
     * 3.工艺信息
     * 4.属性信息
     * @param djBasicsProduct
     * @return
     */
    private Map<String, Object> getProductDetailByProductId(DjBasicsProductTemplate djBasicsProduct){
        String id=djBasicsProduct.getId();
        String address = configUtil.getValue(SysConfig.PUBLIC_DANGJIA_ADDRESS, String.class);
        String[] imgArr = djBasicsProduct.getImage().split(",");
        StringBuilder imgStr = new StringBuilder();
        StringBuilder imgUrlStr = new StringBuilder();
        StringTool.getImages(address, imgArr, imgStr, imgUrlStr);
        djBasicsProduct.setImage(imgStr.toString());
        //单位列表
        List<Unit> linkUnitList = getlinkUnitListByGoodsUnitId(djBasicsProduct.getGoodsId());
        //商品工艺信息
        List<Map<String, Object>> tTechnologymMapList = getTechnologymMapList(id,address);
        //材料商品信息
         if(StringUtils.isNotBlank(djBasicsProduct.getDetailImage())){
                imgArr = djBasicsProduct.getDetailImage().split(",");
                imgStr = new StringBuilder();
                imgUrlStr = new StringBuilder();
                StringTool.getImages(address, imgArr, imgStr, imgUrlStr);
             djBasicsProduct.setDetailImage(imgStr.toString());
          }
        //商品属性值信息
        String strNewValueNameArr = "";
        if (StringUtils.isNotBlank(djBasicsProduct.getValueIdArr())) {
            strNewValueNameArr = getNewValueNameArr(djBasicsProduct.getValueIdArr());
        }
        Map<String, Object> map = BeanUtils.beanToMap(djBasicsProduct);
        map.put("imageUrl", imgUrlStr.toString());
        map.put("newValueNameArr", strNewValueNameArr);
        map.put("tTechnologymMapList", tTechnologymMapList);
        map.put("unitList",linkUnitList);
        map.put("imageUrl",imgUrlStr.toString());
        map.put("id",djBasicsProduct.getId());
        return map;
    }

    /**
     * 获取对应的属性值信息
     * @param valueIdArr
     * @return
     */
    public String getNewValueNameArr(String valueIdArr){
        String strNewValueNameArr = "";
        String[] newValueNameArr = valueIdArr.split(",");
        for (int i = 0; i < newValueNameArr.length; i++) {
            String valueId = newValueNameArr[i];
            if (StringUtils.isNotBlank(valueId)) {
                AttributeValue attributeValue = iAttributeValueMapper.selectByPrimaryKey(valueId);
                if(attributeValue!=null&&StringUtils.isNotBlank(attributeValue.getName())){
                    if (i == 0) {
                        strNewValueNameArr = attributeValue.getName();
                    } else {
                        strNewValueNameArr = strNewValueNameArr + "," + attributeValue.getName();
                    }
                }

            }
        }
        return strNewValueNameArr;
    }

    /**
     * 查询关联的换算单位
     * @param goodsId
     * @return
     */
    private List<Unit> getlinkUnitListByGoodsUnitId(String goodsId){
        List<Unit> linkUnitList = new ArrayList<>();
        BasicsGoods oldGoods = iBasicsGoodsMapper.selectByPrimaryKey(goodsId);
        if(oldGoods!=null&&StringUtils.isNotBlank(oldGoods.getUnitId())){
            Unit unit = iUnitMapper.selectByPrimaryKey(oldGoods.getUnitId());
//            linkUnitList.add(unit);
            if (unit!=null&&unit.getLinkUnitIdArr() != null) {
                String[] linkUnitIdArr = unit.getLinkUnitIdArr().split(",");
                for (String linkUnitId : linkUnitIdArr) {
                    Unit linkUnit = iUnitMapper.selectByPrimaryKey(linkUnitId);
                    linkUnitList.add(linkUnit);
                }
            }
        }


        return linkUnitList;
    }

    /**
     * 查询对应工艺信息
     * @param productId
     * @return
     */
    private List<Map<String, Object>> getTechnologymMapList(String productId,String address){
        List<Technology> pTechnologyList = iTechnologyMapper.queryTechnologyByWgId(productId);
        List<Map<String, Object>> tTechnologymMapList = new ArrayList<>();
        for (Technology t : pTechnologyList) {
            if (t.getImage() == null) {
                continue;
            }
            String[] imgArr = t.getImage().split(",");
            StringBuilder imgStr = new StringBuilder();
            StringBuilder imgUrlStr = new StringBuilder();
            StringTool.getImages(address, imgArr, imgStr, imgUrlStr);
            t.setImage(imgUrlStr.toString());
            Map<String, Object> techMap = BeanUtils.beanToMap(t);
            techMap.put("imageUrl", imgStr.toString());
            techMap.put("sampleImageUrl", address + t.getSampleImage());
            tTechnologymMapList.add(techMap);
        }
        return tTechnologymMapList;
    }


    //根据类别Id查到所有所属货品goods
    public ServerResponse getAllGoodsByCategoryId(String categoryId) {
        try {
            List<BasicsGoods> mapList = iBasicsGoodsMapper.queryByCategoryId(categoryId);
            return ServerResponse.createBySuccess("查询成功", mapList);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponse.createByErrorMessage("查询失败");
        }
    }

    /**
     * 根据货品ID查询商品
     * @param goodsId
     * @return
     */
    public ServerResponse getAllProductByGoodsId(String goodsId) {
        try {
            String address = configUtil.getValue(SysConfig.PUBLIC_DANGJIA_ADDRESS, String.class);
            List<DjBasicsProductTemplate> pList = iBasicsProductTemplateMapper.queryByGoodsId(goodsId);
            List<Map<String, Object>> mapList = new ArrayList<>();
            for (DjBasicsProductTemplate p : pList) {
                if (p.getImage() == null) {
                    continue;
                }
                Map<String, Object> map = getProductDetailByProductId(p);
                mapList.add(map);
            }
            return ServerResponse.createBySuccess("查询成功", mapList);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponse.createByErrorMessage("查询失败");
        }
    }


    /**
     * 根据货品ID查询商品
     * @param goodsId
     * @return
     */
    public ServerResponse getAllProductByGoodsIdLimit12(String goodsId) {
        try {
            String address = configUtil.getValue(SysConfig.PUBLIC_DANGJIA_ADDRESS, String.class);
            List<DjBasicsProductTemplate> pList = iBasicsProductTemplateMapper.queryByGoodsIdLimit12(goodsId);
            List<Map<String, Object>> mapList = new ArrayList<>();
            for (DjBasicsProductTemplate p : pList) {
                if (p.getImage() == null) {
                    continue;
                }
                Map<String, Object> map = getProductDetailByProductId(p);
                mapList.add(map);
            }
            return ServerResponse.createBySuccess("查询成功", mapList);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponse.createByErrorMessage("查询失败");
        }
    }
    public PageInfo queryBasicsProductData( Integer pageNum,Integer pageSize,  String name, String categoryId, String productType, String[] productId) {
        PageHelper.startPage(pageNum, pageSize);
        List<DjBasicsProductTemplate> productList = iBasicsProductTemplateMapper.queryProductData(name, categoryId, productType, productId);
        PageInfo pageResult = new PageInfo(productList);
        return pageResult;
    }


    /**
     * 确认开工选择商品列表
     * @return
     */
    public ServerResponse queryChooseGoods() {
        try {
            List<DjBasicsProductTemplate> djBasicsProducts = iBasicsProductTemplateMapper.queryChooseGoods();
            if(djBasicsProducts.size()<=0)
                return ServerResponse.createByErrorCodeMessage(ServerCode.NO_DATA.getCode(),ServerCode.NO_DATA.getDesc());
            return ServerResponse.createBySuccess("查询成功", djBasicsProducts);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponse.createByErrorMessage("查询失败");
        }
    }


    /**
     * 根据商品id查询标签
     * @param productId
     * @return
     */
    public ServerResponse queryProductLabelsByProductId(String productId) {
        DjBasicsProductTemplate djBasicsProduct = iBasicsProductTemplateMapper.selectByPrimaryKey(productId);
        DjBasicsGoods djBasicsGoods = djBasicsGoodsMapper.selectByPrimaryKey(djBasicsProduct.getGoodsId());
        String s = djBasicsGoods.getLabelIds();
        if(!CommonUtil.isEmpty(s)){
            List<String> strings = Arrays.asList(s.split(","));
            Example example=new Example(DjBasicsLabel.class);
            example.createCriteria().andIn(DjBasicsLabel.ID,strings);
            List<Label> labels = iLabelMapper.selectByExample(example);
            List<DjBasicsLabelDTO> labelDTOS=new ArrayList<>();
            labels.forEach(label -> {
                DjBasicsLabelDTO djBasicsLabelDTO=new DjBasicsLabelDTO();
                djBasicsLabelDTO.setId(label.getId());
                djBasicsLabelDTO.setName(label.getName());
                Example example1=new Example(DjBasicsLabelValue.class);
                example1.createCriteria().andEqualTo(DjBasicsLabelValue.LABEL_ID,label.getId());
                djBasicsLabelDTO.setLabelValueList(djBasicsLabelValueMapper.selectByExample(example1));
                labelDTOS.add(djBasicsLabelDTO);
            });
            return ServerResponse.createBySuccess("查询成功",labelDTOS);
        }else{
            return ServerResponse.createByErrorCodeMessage(ServerCode.NO_DATA.getCode(), ServerCode.NO_DATA.getDesc());
        }
    }

}