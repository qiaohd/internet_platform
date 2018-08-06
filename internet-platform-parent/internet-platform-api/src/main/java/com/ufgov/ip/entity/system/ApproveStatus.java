package com.ufgov.ip.entity.system;

public interface ApproveStatus {

	/**
	 * 以下是审批状态
	 */
	public static final int STAFF_SUBMIT=1;//请假单已提交
	public static final int DEPT_APPROVE=2;//部门经理已审批
	public static final int HR_APPROVE=3;//人事经理已审批

	
	/**
	 * 以下是审批的反馈结果：同意/驳回/初始信息
	 */
	public static final int FEEDBACK_INIT=0;//刚提交申请，没有反馈状态
	public static final int FEEDBACK_AGREE=1;//同意
	public static final int FEEDBACK_DISAGREE=2;//驳回
	
	
	public static final int SUBMIT=1;//请假单已提交
	public static final int APPROVE=2;//部门经理已审批
	public static final int DIS_APPROVE=3;//人事经理已审批
	
	
	
	
	
	
	
}
