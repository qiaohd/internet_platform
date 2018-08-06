package com.ufgov.ip.dubbo.rpc.filter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcException;
import com.ufgov.ip.common.ContextThreadLocal;
import com.yonyou.iuap.context.InvocationInfoProxy;

@Activate(group = Constants.CONSUMER, order = -9999)
public class ConsumerMultiTenantFilter implements Filter {

	@Override
	public Result invoke(Invoker<?> invoker, Invocation invocation)
			throws RpcException {
		String tenantId = InvocationInfoProxy.getTenantid();
		RpcContext.getContext().getAttachments().put(ContextThreadLocal.TENANT_ID,null == tenantId ? "" : tenantId);
		try {
			return invoker.invoke(invocation);
		} finally {
			RpcContext.removeContext();
		}
	}

}
