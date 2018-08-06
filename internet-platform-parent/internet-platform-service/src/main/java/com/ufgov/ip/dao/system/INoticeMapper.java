package com.ufgov.ip.dao.system;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ufgov.ip.entity.system.IpNotice;
import com.yonyou.iuap.persistence.mybatis.anotation.MyBatisRepository;

@MyBatisRepository
public interface INoticeMapper {

	/**
	 * 保存公告
	 */
	
     public void saveNotice(IpNotice ipNotice);
     
     
     /**
      * 编辑公告
      * @param ipNotice
      */
     public void updateNotice(IpNotice ipNotice);


     /**
      * 获得所有公告
      */
	public List<IpNotice> getAllNotice(@Param("ipNotice")IpNotice ipNotice,@Param("dbType") String dbType);


	/**
	 * 获得公告详情
	 * @param noticeId
	 * @return
	 */
	public IpNotice getNoticeDetail(String noticeId);

    /**
     * 删除当前公告
     * @param noticeId
     */
	public void deleteNotice(String noticeId);


	/**
	 * 通过关键字查找公告
	 * @param keywords
	 * @param isPublish 
	 * @param hirerId 
	 * @return
	 */
	public List<IpNotice> findBykeywords(@Param("ipNotice")IpNotice ipNotice, @Param("dbType") String dbType);


	public Integer findRecordNum(@Param("ipNotice") IpNotice ipNotice, @Param("dbType") String dbType);


	public List<IpNotice> getAllNoticeWithSP(@Param("ipNotice")IpNotice ipNotice, @Param("dbType") String dbType);
	
	
	
}
