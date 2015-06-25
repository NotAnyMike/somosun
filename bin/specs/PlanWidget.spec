package specs

import com.uibinder.index.client.widget.PlanWidget

describe PlanWidget {
	fact "A Plan has at least one semester"
	fact "A Plan does not has a repeated subject"
	fact "A plan has its semester order ascending"
}