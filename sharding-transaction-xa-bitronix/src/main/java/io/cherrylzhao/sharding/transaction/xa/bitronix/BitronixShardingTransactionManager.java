/*
 * Copyright 2016-2018 shardingsphere.io.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package io.cherrylzhao.sharding.transaction.xa.bitronix;

import bitronix.tm.BitronixTransactionManager;
import bitronix.tm.TransactionManagerServices;
import bitronix.tm.resource.ResourceRegistrar;
import io.shardingsphere.transaction.xa.spi.SingleXAResource;
import io.shardingsphere.transaction.xa.spi.XATransactionManager;
import lombok.SneakyThrows;

import javax.sql.XADataSource;

/**
 * Bitronix sharding transaction manager.
 *
 * @author zhaojun
 */
public class BitronixShardingTransactionManager implements XATransactionManager {
    
    private final BitronixTransactionManager bitronixTransactionManager = TransactionManagerServices.getTransactionManager();
    
    public void init() {
    }
    
    @SneakyThrows
    public void registerRecoveryResource(String dataSourceName, XADataSource xaDataSource) {
        ResourceRegistrar.register(new BitronixRecoveryResource(dataSourceName, xaDataSource));
    }
    
    @SneakyThrows
    public void removeRecoveryResource(String dataSourceName, XADataSource xaDataSource) {
        ResourceRegistrar.unregister(new BitronixRecoveryResource(dataSourceName, xaDataSource));
    }
    
    @SneakyThrows
    public void enlistResource(SingleXAResource singleXAResource) {
        bitronixTransactionManager.getTransaction().enlistResource(singleXAResource);
    }
    
    @SneakyThrows
    public void begin() {
        bitronixTransactionManager.begin();
    }
    
    @SneakyThrows
    public void commit() {
        bitronixTransactionManager.commit();
    }
    
    @SneakyThrows
    public void rollback() {
        bitronixTransactionManager.rollback();
    }
    
    @SneakyThrows
    public int getStatus() {
        return bitronixTransactionManager.getStatus();
    }
    
    public void close() throws Exception {
        bitronixTransactionManager.shutdown();
    }
}
