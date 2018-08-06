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

@Activate(group = Constants.PROVIDER, order = -9999)
public class ProviderMultiTenantFilter implements Filter {

	@Override
	public Result invoke(Invoker<?> invoker, Invocation invocation)
			throws RpcException {		
		String tenantId = RpcContext.getContext().getAttachments().get(ContextThreadLocal.TENANT_ID);
		ContextThreadLocal.setCurrentTenant(tenantId);
		try {
			return invoker.invoke(invocation);
		} finally {
			RpcContext.removeContext();
		}
	}

}
