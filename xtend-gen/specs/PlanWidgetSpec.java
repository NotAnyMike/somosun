package specs;

import org.jnario.runner.ExampleGroupRunner;
import org.jnario.runner.Named;
import org.jnario.runner.Order;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

@Named("PlanWidget")
@RunWith(ExampleGroupRunner.class)
@SuppressWarnings("all")
public class PlanWidgetSpec {
  @Test
  @Ignore
  @Named("A Plan has at least one semester [PENDING]")
  @Order(1)
  public void _aPlanHasAtLeastOneSemester() throws Exception {
  }
  
  @Test
  @Ignore
  @Named("A Plan does not has a repeated subject [PENDING]")
  @Order(2)
  public void _aPlanDoesNotHasARepeatedSubject() throws Exception {
  }
  
  @Test
  @Ignore
  @Named("A plan has its semester order ascending [PENDING]")
  @Order(3)
  public void _aPlanHasItsSemesterOrderAscending() throws Exception {
  }
}
