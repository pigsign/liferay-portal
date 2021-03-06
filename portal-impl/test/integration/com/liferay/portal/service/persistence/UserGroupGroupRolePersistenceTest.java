/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.service.persistence;

import com.liferay.portal.NoSuchUserGroupGroupRoleException;
import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ProjectionFactoryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.test.ExecutionTestListeners;
import com.liferay.portal.kernel.util.IntegerWrapper;
import com.liferay.portal.model.ModelListener;
import com.liferay.portal.model.UserGroupGroupRole;
import com.liferay.portal.service.UserGroupGroupRoleLocalServiceUtil;
import com.liferay.portal.service.persistence.BasePersistence;
import com.liferay.portal.service.persistence.PersistenceExecutionTestListener;
import com.liferay.portal.test.LiferayPersistenceIntegrationJUnitTestRunner;
import com.liferay.portal.test.persistence.TransactionalPersistenceAdvice;
import com.liferay.portal.util.test.RandomTestUtil;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import java.io.Serializable;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Brian Wing Shun Chan
 */
@ExecutionTestListeners(listeners =  {
	PersistenceExecutionTestListener.class})
@RunWith(LiferayPersistenceIntegrationJUnitTestRunner.class)
public class UserGroupGroupRolePersistenceTest {
	@Before
	public void setUp() {
		_modelListeners = _persistence.getListeners();

		for (ModelListener<UserGroupGroupRole> modelListener : _modelListeners) {
			_persistence.unregisterListener(modelListener);
		}
	}

	@After
	public void tearDown() throws Exception {
		Map<Serializable, BasePersistence<?>> basePersistences = _transactionalPersistenceAdvice.getBasePersistences();

		Set<Serializable> primaryKeys = basePersistences.keySet();

		for (Serializable primaryKey : primaryKeys) {
			BasePersistence<?> basePersistence = basePersistences.get(primaryKey);

			try {
				basePersistence.remove(primaryKey);
			}
			catch (Exception e) {
				if (_log.isDebugEnabled()) {
					_log.debug("The model with primary key " + primaryKey +
						" was already deleted");
				}
			}
		}

		_transactionalPersistenceAdvice.reset();

		for (ModelListener<UserGroupGroupRole> modelListener : _modelListeners) {
			_persistence.registerListener(modelListener);
		}
	}

	@Test
	public void testCreate() throws Exception {
		UserGroupGroupRolePK pk = new UserGroupGroupRolePK(RandomTestUtil.nextLong(),
				RandomTestUtil.nextLong(), RandomTestUtil.nextLong());

		UserGroupGroupRole userGroupGroupRole = _persistence.create(pk);

		Assert.assertNotNull(userGroupGroupRole);

		Assert.assertEquals(userGroupGroupRole.getPrimaryKey(), pk);
	}

	@Test
	public void testRemove() throws Exception {
		UserGroupGroupRole newUserGroupGroupRole = addUserGroupGroupRole();

		_persistence.remove(newUserGroupGroupRole);

		UserGroupGroupRole existingUserGroupGroupRole = _persistence.fetchByPrimaryKey(newUserGroupGroupRole.getPrimaryKey());

		Assert.assertNull(existingUserGroupGroupRole);
	}

	@Test
	public void testUpdateNew() throws Exception {
		addUserGroupGroupRole();
	}

	@Test
	public void testUpdateExisting() throws Exception {
		UserGroupGroupRolePK pk = new UserGroupGroupRolePK(RandomTestUtil.nextLong(),
				RandomTestUtil.nextLong(), RandomTestUtil.nextLong());

		UserGroupGroupRole newUserGroupGroupRole = _persistence.create(pk);

		newUserGroupGroupRole.setMvccVersion(RandomTestUtil.nextLong());

		_persistence.update(newUserGroupGroupRole);

		UserGroupGroupRole existingUserGroupGroupRole = _persistence.findByPrimaryKey(newUserGroupGroupRole.getPrimaryKey());

		Assert.assertEquals(existingUserGroupGroupRole.getMvccVersion(),
			newUserGroupGroupRole.getMvccVersion());
		Assert.assertEquals(existingUserGroupGroupRole.getUserGroupId(),
			newUserGroupGroupRole.getUserGroupId());
		Assert.assertEquals(existingUserGroupGroupRole.getGroupId(),
			newUserGroupGroupRole.getGroupId());
		Assert.assertEquals(existingUserGroupGroupRole.getRoleId(),
			newUserGroupGroupRole.getRoleId());
	}

	@Test
	public void testCountByUserGroupId() {
		try {
			_persistence.countByUserGroupId(RandomTestUtil.nextLong());

			_persistence.countByUserGroupId(0L);
		}
		catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testCountByGroupId() {
		try {
			_persistence.countByGroupId(RandomTestUtil.nextLong());

			_persistence.countByGroupId(0L);
		}
		catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testCountByRoleId() {
		try {
			_persistence.countByRoleId(RandomTestUtil.nextLong());

			_persistence.countByRoleId(0L);
		}
		catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testCountByU_G() {
		try {
			_persistence.countByU_G(RandomTestUtil.nextLong(),
				RandomTestUtil.nextLong());

			_persistence.countByU_G(0L, 0L);
		}
		catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testCountByG_R() {
		try {
			_persistence.countByG_R(RandomTestUtil.nextLong(),
				RandomTestUtil.nextLong());

			_persistence.countByG_R(0L, 0L);
		}
		catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testFindByPrimaryKeyExisting() throws Exception {
		UserGroupGroupRole newUserGroupGroupRole = addUserGroupGroupRole();

		UserGroupGroupRole existingUserGroupGroupRole = _persistence.findByPrimaryKey(newUserGroupGroupRole.getPrimaryKey());

		Assert.assertEquals(existingUserGroupGroupRole, newUserGroupGroupRole);
	}

	@Test
	public void testFindByPrimaryKeyMissing() throws Exception {
		UserGroupGroupRolePK pk = new UserGroupGroupRolePK(RandomTestUtil.nextLong(),
				RandomTestUtil.nextLong(), RandomTestUtil.nextLong());

		try {
			_persistence.findByPrimaryKey(pk);

			Assert.fail(
				"Missing entity did not throw NoSuchUserGroupGroupRoleException");
		}
		catch (NoSuchUserGroupGroupRoleException nsee) {
		}
	}

	@Test
	public void testFetchByPrimaryKeyExisting() throws Exception {
		UserGroupGroupRole newUserGroupGroupRole = addUserGroupGroupRole();

		UserGroupGroupRole existingUserGroupGroupRole = _persistence.fetchByPrimaryKey(newUserGroupGroupRole.getPrimaryKey());

		Assert.assertEquals(existingUserGroupGroupRole, newUserGroupGroupRole);
	}

	@Test
	public void testFetchByPrimaryKeyMissing() throws Exception {
		UserGroupGroupRolePK pk = new UserGroupGroupRolePK(RandomTestUtil.nextLong(),
				RandomTestUtil.nextLong(), RandomTestUtil.nextLong());

		UserGroupGroupRole missingUserGroupGroupRole = _persistence.fetchByPrimaryKey(pk);

		Assert.assertNull(missingUserGroupGroupRole);
	}

	@Test
	public void testActionableDynamicQuery() throws Exception {
		final IntegerWrapper count = new IntegerWrapper();

		ActionableDynamicQuery actionableDynamicQuery = UserGroupGroupRoleLocalServiceUtil.getActionableDynamicQuery();

		actionableDynamicQuery.setPerformActionMethod(new ActionableDynamicQuery.PerformActionMethod() {
				@Override
				public void performAction(Object object) {
					UserGroupGroupRole userGroupGroupRole = (UserGroupGroupRole)object;

					Assert.assertNotNull(userGroupGroupRole);

					count.increment();
				}
			});

		actionableDynamicQuery.performActions();

		Assert.assertEquals(count.getValue(), _persistence.countAll());
	}

	@Test
	public void testDynamicQueryByPrimaryKeyExisting()
		throws Exception {
		UserGroupGroupRole newUserGroupGroupRole = addUserGroupGroupRole();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(UserGroupGroupRole.class,
				UserGroupGroupRole.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("id.userGroupId",
				newUserGroupGroupRole.getUserGroupId()));
		dynamicQuery.add(RestrictionsFactoryUtil.eq("id.groupId",
				newUserGroupGroupRole.getGroupId()));
		dynamicQuery.add(RestrictionsFactoryUtil.eq("id.roleId",
				newUserGroupGroupRole.getRoleId()));

		List<UserGroupGroupRole> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		UserGroupGroupRole existingUserGroupGroupRole = result.get(0);

		Assert.assertEquals(existingUserGroupGroupRole, newUserGroupGroupRole);
	}

	@Test
	public void testDynamicQueryByPrimaryKeyMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(UserGroupGroupRole.class,
				UserGroupGroupRole.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("id.userGroupId",
				RandomTestUtil.nextLong()));
		dynamicQuery.add(RestrictionsFactoryUtil.eq("id.groupId",
				RandomTestUtil.nextLong()));
		dynamicQuery.add(RestrictionsFactoryUtil.eq("id.roleId",
				RandomTestUtil.nextLong()));

		List<UserGroupGroupRole> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testDynamicQueryByProjectionExisting()
		throws Exception {
		UserGroupGroupRole newUserGroupGroupRole = addUserGroupGroupRole();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(UserGroupGroupRole.class,
				UserGroupGroupRole.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property(
				"id.userGroupId"));

		Object newUserGroupId = newUserGroupGroupRole.getUserGroupId();

		dynamicQuery.add(RestrictionsFactoryUtil.in("id.userGroupId",
				new Object[] { newUserGroupId }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Object existingUserGroupId = result.get(0);

		Assert.assertEquals(existingUserGroupId, newUserGroupId);
	}

	@Test
	public void testDynamicQueryByProjectionMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(UserGroupGroupRole.class,
				UserGroupGroupRole.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property(
				"id.userGroupId"));

		dynamicQuery.add(RestrictionsFactoryUtil.in("id.userGroupId",
				new Object[] { RandomTestUtil.nextLong() }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	protected UserGroupGroupRole addUserGroupGroupRole()
		throws Exception {
		UserGroupGroupRolePK pk = new UserGroupGroupRolePK(RandomTestUtil.nextLong(),
				RandomTestUtil.nextLong(), RandomTestUtil.nextLong());

		UserGroupGroupRole userGroupGroupRole = _persistence.create(pk);

		userGroupGroupRole.setMvccVersion(RandomTestUtil.nextLong());

		_persistence.update(userGroupGroupRole);

		return userGroupGroupRole;
	}

	private static Log _log = LogFactoryUtil.getLog(UserGroupGroupRolePersistenceTest.class);
	private ModelListener<UserGroupGroupRole>[] _modelListeners;
	private UserGroupGroupRolePersistence _persistence = (UserGroupGroupRolePersistence)PortalBeanLocatorUtil.locate(UserGroupGroupRolePersistence.class.getName());
	private TransactionalPersistenceAdvice _transactionalPersistenceAdvice = (TransactionalPersistenceAdvice)PortalBeanLocatorUtil.locate(TransactionalPersistenceAdvice.class.getName());
}