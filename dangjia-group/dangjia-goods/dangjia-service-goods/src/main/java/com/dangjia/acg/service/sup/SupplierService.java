package com.dangjia.acg.service.sup;

import com.dangjia.acg.common.response.ServerResponse;
import com.dangjia.acg.mapper.basics.IProductMapper;
import com.dangjia.acg.mapper.sup.ISupplierMapper;
import com.dangjia.acg.modle.attribute.AttributeValue;
import com.dangjia.acg.modle.basics.Product;
import com.dangjia.acg.modle.sup.Supplier;
import com.dangjia.acg.modle.sup.SupplierProduct;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
   * @类 名： SupplierServiceImpl
   * @功能描述： TODO
   * @作者信息： zmj
   * @创建时间： 2018-9-17下午3:28:20
 */
@Service
public class SupplierService {
	@Autowired
	private ISupplierMapper iSupplierMapper;
	@Autowired
	private IProductMapper iProductMapper;
	/**
	 * 新增供应商
	 * <p>Title: insertSupplier</p>
	 * <p>Description: </p>
	 * @param name
	 * @param address
	 * @param telephone
	 * @param checkPeople
	 * @param gender
	 * @param email
	 * @param notice
	 * @param supplierLevel
	 * @param state
	 */
	public ServerResponse insertSupplier(String name, String address, String telephone, String checkPeople, Integer gender,
										 String email, String notice, Integer supplierLevel, Integer state){
		try{
			Supplier supplier=new Supplier();
			supplier.setName(name);//名称
			supplier.setAddress(address);//地址
			supplier.setTelephone(telephone);//联系电话
			supplier.setCheckpeople(checkPeople);//联系人姓名
			supplier.setGender(gender);//1男 2女   0 未选
			supplier.setEmail(email);
			supplier.setNotice(notice);
			supplier.setSupplierLevel(supplierLevel);//级别
			supplier.setState(state);//供应商状态  1正常供货 2停止供货
			supplier.setCreateDate(new Date());
			supplier.setModifyDate(new Date());
			iSupplierMapper.insert(supplier);
			return ServerResponse.createBySuccessMessage("新增成功");
		}catch (Exception e) {
			e.printStackTrace();
			return ServerResponse.createByErrorMessage("新增失败");
		}
	}
	
	/**
	 * 修改供应商
	 * <p>Title: insertSupplier</p>
	 * <p>Description: </p>
	 * @param name
	 * @param address
	 * @param telephone
	 * @param checkPeople
	 * @param gender
	 * @param email
	 * @param notice
	 * @param supplier_level
	 * @param state
	 */
	public ServerResponse updateSupplier(String id,String name,String address,String telephone,String checkPeople,Integer gender,
    		String email,String notice,Integer supplier_level,Integer state){
		try{
			Supplier supplier=new Supplier();
			supplier.setId(id);
			supplier.setName(name);//名称
			supplier.setAddress(address);//地址
			supplier.setTelephone(telephone);//联系电话
			supplier.setCheckpeople(checkPeople);//联系人姓名
			supplier.setGender(gender);//1男 2女   0 未选
			supplier.setEmail(email);
			supplier.setNotice(notice);
			supplier.setSupplierLevel(supplier_level);//级别
			supplier.setState(state);//供应商状态  1正常供货 2停止供货
			supplier.setModifyDate(new Date());
			iSupplierMapper.updateByPrimaryKeySelective(supplier);
			return ServerResponse.createBySuccessMessage("修改成功");
		}catch (Exception e) {
			e.printStackTrace();
			return ServerResponse.createByErrorMessage("修改失败");
		}
	}
    /**
     * 查询所有供应商
     */
	public ServerResponse<PageInfo> querySupplierList(Integer pageNum,Integer pageSize) {
		try{
			if (pageNum == null) {
				pageNum = 1;
			}
			if (pageSize == null) {
				pageSize = 10;
			}
			PageHelper.startPage(pageNum, pageSize);
			List<Map<String, Object>> mapList=new ArrayList<Map<String, Object>>();//返回list
			List<Supplier> supplierList=iSupplierMapper.query();
			for(Supplier supplier:supplierList){
				Map<String, Object> map=new HashMap<String, Object>();
				map.put("id", supplier.getId());
				map.put("name", supplier.getName());//供应商名称
				map.put("address", supplier.getAddress());//地址
				map.put("telephone", supplier.getTelephone());//联系电话
				map.put("checkpeople", supplier.getCheckpeople());//联系人
				map.put("email", supplier.getEmail());//电子邮件
				map.put("notice", supplier.getNotice());//发货须知
				map.put("supplierLevel", supplier.getSupplierLevel());//供应商级别
				map.put("gender", supplier.getGender());//联系人性别 1男 2女
				map.put("countGoods", iSupplierMapper.getSupplierProductByGid(supplier.getId())==null?"0":iSupplierMapper.getSupplierProductByGid(supplier.getId()));//供应的货品种类
				map.put("countAttribute", iSupplierMapper.getSupplierProductByAid(supplier.getId())==null?"0":iSupplierMapper.getSupplierProductByAid(supplier.getId()));//供应的商品种类
				map.put("countStock", iSupplierMapper.getSupplierProductByStock(supplier.getId()));//库存小于50的
				map.put("state", supplier.getState());//供应商状态  1正常供货 2停止供货
				SimpleDateFormat  sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				map.put("createDate", sdf.format(supplier.getCreateDate()==null?new Date():supplier.getCreateDate()));
				mapList.add(map);
			}
			PageInfo pageResult = new PageInfo(supplierList);
			pageResult.setList(mapList);
			return ServerResponse.createBySuccess("查询成功",pageResult);
		}catch (Exception e) {
			e.printStackTrace();
			return ServerResponse.createByErrorMessage("查询失败");
		}
	}
	/**
     * 查询所有商品供应关系0:仅供应商品;1:所有商品
     */
	public ServerResponse querySupplierProduct(int type,String supplierId,String categoryId) {
		try{
			List<Map<String, Object>> listMap=new ArrayList<>();
			if(type==0){//仅供应商品
				List<Product> pList=iSupplierMapper.querySupplierProduct(supplierId);
				for(Product product:pList){
					Map<String, Object> gmap=new HashMap<String, Object>();
					gmap.put("pId", product.getId());//商品id
					gmap.put("categoryName", product.getName());//类别名称
					gmap.put("goodsName", product.getName());//商品名称
					gmap.put("productName", product.getName());//商品名称
					gmap.put("productName", product.getName());//商品名称
					gmap.put("isSupply", 1);//是否供应；0停供，1供应
					SupplierProduct supplierProduct=iSupplierMapper.querySupplierProductRelation(product.getId(),supplierId);
					if(supplierProduct==null){
						gmap.put("price", 0);//供应价格
						gmap.put("stock", 0);//库存
					}else{
						gmap.put("price", supplierProduct.getPrice());//供应价格
						gmap.put("stock", supplierProduct.getStock());//库存
					}
					List<AttributeValue> aveList=iProductMapper.queryProductAttributeByPid(product.getId());//查询货品关联属性
					StringBuffer aveString=new StringBuffer();
					for(int i=0;i<aveList.size();i++){//拼接属性选项
						if(i==aveList.size()-1){
						    aveString.append(aveList.get(i).getName());
						}else{
							aveString.append(aveList.get(i).getName());
							aveString.append(",");
						}
				    }
					gmap.put("aveString", aveString);//属性选项拼接串
					listMap.add(gmap);
				}
			}else{//所有商品
				List<Product> pList=iProductMapper.query(categoryId);//查询所有货品
				for(Product product:pList){
					Map<String, Object> gmap=new HashMap<String, Object>();
					gmap.put("pId", product.getId());//商品id
					gmap.put("categoryName", product.getName());//类别名称
					gmap.put("goodsName", product.getName());//商品名称
					gmap.put("productName", product.getName());//商品名称
					SupplierProduct supplierProduct=iSupplierMapper.querySupplierProductRelation(product.getId(),supplierId);
					if(supplierProduct==null){
						gmap.put("isSupply", 0);//是否供应；0停供，1供应
						gmap.put("price", 0);//供应价格
						gmap.put("stock", 0);//库存
					}else{
						gmap.put("isSupply", supplierProduct.getIsSupply());//是否供应；0停供，1供应
						gmap.put("price", supplierProduct.getPrice());//供应价格
						gmap.put("stock", supplierProduct.getStock());//库存
					}
					List<AttributeValue> aveList=iProductMapper.queryProductAttributeByPid(product.getId());//查询货品关联属性
					StringBuffer aveString=new StringBuffer();
					for(int i=0;i<aveList.size();i++){//拼接属性选项
						if(i==aveList.size()-1){
						    aveString.append(aveList.get(i).getName());
						}else{
							aveString.append(aveList.get(i).getName());
							aveString.append(",");
						}
				    }
					gmap.put("aveString", aveString);//属性选项拼接串
					listMap.add(gmap);
				}
			}
			return ServerResponse.createBySuccess("查询成功",listMap);
		}catch (Exception e) {
			e.printStackTrace();
			return ServerResponse.createByErrorMessage("查询失败");
		}
	}
	/**
     * 保存供应商与货品供应关系
     */
	public ServerResponse saveSupplierProduct(String productId, String supplierId,
			 Double price, Double stock, Integer isSupply) {
		try{
			//根据供应商、商品、属性查询对应关系
			SupplierProduct supplierProduct=iSupplierMapper.querySupplierProductRelation(productId,supplierId);
			if(supplierProduct==null){//新增
				SupplierProduct sp=new SupplierProduct();
				sp.setProductId(productId);
				sp.setSupplierId(supplierId);
				sp.setPrice(price);//价格
				sp.setStock(stock);//库存
				sp.setIsSupply(isSupply);//是否供应；0停供，1供应
				sp.setCreateDate(new Date());
				sp.setModifyDate(new Date());
				iSupplierMapper.insertSupplierProduct(sp);
			}else{//保存
				SupplierProduct sp=new SupplierProduct();
				sp.setProductId(productId);
				sp.setSupplierId(supplierId);
				sp.setPrice(price);//价格
				sp.setStock(stock);//库存
				sp.setIsSupply(isSupply);//是否供应；0停供，1供应
				sp.setModifyDate(new Date());
				iSupplierMapper.updateSupplierProduct(sp);
			}
			return ServerResponse.createBySuccessMessage("保存成功");
		}catch (Exception e) {
			e.printStackTrace();
			return ServerResponse.createByErrorMessage("保存失败");
		}
	}
}
