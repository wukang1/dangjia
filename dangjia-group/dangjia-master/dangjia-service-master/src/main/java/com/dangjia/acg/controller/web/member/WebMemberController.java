package com.dangjia.acg.controller.web.member;

import com.dangjia.acg.api.web.member.WebMemberAPI;
import com.dangjia.acg.common.annotation.ApiMethod;
import com.dangjia.acg.common.model.PageDTO;
import com.dangjia.acg.common.response.ServerResponse;
import com.dangjia.acg.modle.member.Customer;
import com.dangjia.acg.modle.member.CustomerRecord;
import com.dangjia.acg.modle.member.Member;
import com.dangjia.acg.modle.member.MemberLabel;
import com.dangjia.acg.service.member.CustomerRecordService;
import com.dangjia.acg.service.member.CustomerService;
import com.dangjia.acg.service.member.MemberLabelService;
import com.dangjia.acg.service.member.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * author: Ronalcheng
 * Date: 2018/11/3 0003
 * Time: 16:35
 */
@RestController
public class WebMemberController implements WebMemberAPI {

    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberLabelService memberLabelService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private CustomerRecordService customerRecordService;

    @Override
    @ApiMethod
    public ServerResponse getMemberList(HttpServletRequest request, PageDTO pageDTO, Integer stage, String memberNickName, String parentId, String childId) {
        return memberService.getMemberList(pageDTO, stage, memberNickName, parentId, childId);
    }

    @Override
    @ApiMethod
    public ServerResponse setMember(HttpServletRequest request, Member member) {
        return memberService.setMember(member);
    }

    @Override
    @ApiMethod
    public ServerResponse getMemberLabelList(HttpServletRequest request, PageDTO pageDTO) {
        return memberLabelService.getMemberLabelList(pageDTO);
    }

    @Override
    @ApiMethod
    public ServerResponse setMemberLabel(HttpServletRequest request, String jsonStr) {
        return memberLabelService.setMemberLabel(jsonStr);
    }

    @Override
    @ApiMethod
    public ServerResponse getCustomerRecordList(HttpServletRequest request, PageDTO pageDTO, String memberId) {
        return customerRecordService.getCustomerRecordList(pageDTO, memberId);
    }

    @Override
    @ApiMethod
    public ServerResponse addCustomerRecord(HttpServletRequest request, CustomerRecord customerRecord) {
        return customerRecordService.addCustomerRecord(customerRecord);
    }

    @Override
    @ApiMethod
    public ServerResponse setMemberCustomer(HttpServletRequest request, Customer customer) {
        return customerService.setMemberCustomer(customer);
    }
}
