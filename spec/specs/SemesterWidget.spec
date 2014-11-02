package specs

import com.uibinder.index.client.widget.SemesterWidget

describe SemesterWidget {
	fact "A semester that is not the last has at least one subject"
	fact "A semester has one position"
	fact "A semester does not have a repeated Subjects"
}