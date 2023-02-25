package org.nakedobjects.plugins.headless.embedded;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.plugins.headless.applib.DisabledException;
import org.nakedobjects.plugins.headless.applib.HeadlessViewer;
import org.nakedobjects.plugins.headless.applib.HiddenException;
import org.nakedobjects.plugins.headless.applib.InvalidException;
import org.nakedobjects.plugins.headless.embedded.EmbeddedContext;
import org.nakedobjects.plugins.headless.embedded.NakedObjectsMetaModel;
import org.nakedobjects.plugins.headless.embedded.dom.claim.ClaimRepositoryImpl;
import org.nakedobjects.plugins.headless.embedded.dom.employee.Employee;
import org.nakedobjects.plugins.headless.embedded.dom.employee.EmployeeRepositoryImpl;
import org.nakedobjects.plugins.headless.embedded.internal.PersistenceState;


@RunWith(JMock.class)
public class GivenEmbeddedViewerAndPersistentDomainObject {
	
	private Mockery mockery = new JUnit4Mockery();
	
	private EmbeddedContext mockContext;

	private AuthenticationSession mockAuthenticationSession;

	private NakedObjectsMetaModel metaModel;

	private HeadlessViewer viewer;

	private Employee employeeDO;

	private Employee employeeVO;
	
	
	@Before
	public void setUp() {
		
		employeeDO = new Employee();
		employeeDO.setName("Smith");
		
		mockContext = mockery.mock(EmbeddedContext.class);
		mockAuthenticationSession = mockery.mock(AuthenticationSession.class);


		mockery.checking(new Expectations(){{
			allowing(mockContext).getPersistenceState(with(any(Employee.class)));
			will(returnValue(PersistenceState.PERSISTENT));
			
			allowing(mockContext).getPersistenceState(with(any(String.class)));
			will(returnValue(PersistenceState.STANDALONE));
			
			allowing(mockContext).getAuthenticationSession();
			will(returnValue(mockAuthenticationSession));
		}});

		metaModel = new NakedObjectsMetaModel(mockContext, EmployeeRepositoryImpl.class, ClaimRepositoryImpl.class);
		metaModel.init();
		
		viewer = metaModel.getViewer();
	}
	
	@Test
	public void shouldBeAbleToGetViewOfDomainObject() {
		employeeVO = viewer.view(employeeDO);
		assertThat(employeeVO, is(notNullValue()));
	}

	
	@Test
	public void shouldBeAbleToReadVisibleProperty() {
		employeeVO = viewer.view(employeeDO);
		
		assertThat(employeeVO.getName(), is(employeeDO.getName()));
	}

	@Test(expected=HiddenException.class)
	public void shouldNotBeAbleToViewHiddenProperty() {
		employeeVO = viewer.view(employeeDO);
		
		employeeDO.whetherHideName = true;
		employeeVO.getName(); // should throw exception
	}


	@Test
	public void shouldBeAbleToModifyEnabledPropertyUsingSetter() {
		employeeVO = viewer.view(employeeDO);
		
		employeeVO.setName("Jones");
		assertThat(employeeDO.getName(), is("Jones"));
		assertThat(employeeVO.getName(), is(employeeDO.getName()));
	}

	@Test(expected=DisabledException.class)
	public void shouldNotBeAbleToModifyDisabledProperty() {
		employeeVO = viewer.view(employeeDO);
		
		employeeDO.reasonDisableName = "sorry, no change allowed";
		employeeVO.setName("Jones");
	}


	@Test(expected=UnsupportedOperationException.class)
	public void shouldNotBeAbleToModifyPropertyUsingModify() {
		employeeVO = viewer.view(employeeDO);
		
		employeeVO.modifyName("Jones"); // should throw exception
	}

	@Test(expected=UnsupportedOperationException.class)
	public void shouldNotBeAbleToModifyPropertyUsingClear() {
		employeeVO = viewer.view(employeeDO);
		
		employeeVO.clearName(); // should throw exception
	}


	@Test(expected=InvalidException.class)
	public void shouldNotBeAbleToModifyPropertyIfInvalid() {
		employeeVO = viewer.view(employeeDO);
		
		employeeDO.reasonValidateName = "sorry, invalid data";
		employeeVO.setName("Jones");
	}


	@Test(expected=DisabledException.class)
	public void shouldNotBeAbleToModifyPropertyForTransientOnly() {
		employeeVO = viewer.view(employeeDO);
		
		employeeVO.setPassword("12345678");
	}

	
	
	@Ignore("incomplete")
	@Test
	public void shouldBeAbleToInjectIntoDomainObjects() {
		
		// TODO: also ... be able to inject EmbeddedViewer as a service itself, if required.
		
		employeeVO.setPassword("12345678");
		
	}

}
