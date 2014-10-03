package specs;

import org.jnario.runner.ExampleGroupRunner;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

@Named("SemesterWidget")
@RunWith(ExampleGroupRunner.class)
@SuppressWarnings("all")
public class SemesterWidgetSpec {
  @Test
  @Ignore
  @Named("A semester that is not the last has at least one subject [PENDING]")
  @Order(1)
  public void _aSemesterThatIsNotTheLastHasAtLeastOneSubject() throws Exception {
  }
  
  @Test
  @Ignore
  @Named("A semester has one position [PENDING]")
  @Order(2)
  public void _aSemesterHasOnePosition() throws Exception {
  }
  
  @Test
  @Ignore
  @Named("A semester does not have a repeated Subjects [PENDING]")
  @Order(3)
  public void _aSemesterDoesNotHaveARepeatedSubjects() throws Exception {
  }
}
