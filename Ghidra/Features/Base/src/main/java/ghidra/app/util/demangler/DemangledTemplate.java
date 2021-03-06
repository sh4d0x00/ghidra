/* ###
 * IP: GHIDRA
 * REVIEWED: YES
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ghidra.app.util.demangler;

import ghidra.program.model.data.DataType;
import ghidra.util.Msg;

import java.util.ArrayList;
import java.util.List;

import util.demangler.GenericDemangledDataType;
import util.demangler.GenericDemangledTemplate;

public class DemangledTemplate implements ParameterReceiver {
	private List<DemangledDataType> parameters = new ArrayList<DemangledDataType>();

	public DemangledTemplate() {
	}

	DemangledTemplate(GenericDemangledTemplate template) {
		List<GenericDemangledDataType> genericParameters = template.getParameters();
		for (GenericDemangledDataType parameter : genericParameters) {
			parameters.add((DemangledDataType) DemangledObjectFactory.convert(parameter));
		}
	}

	@Override
	public void addParameter(DemangledDataType parameter) {
		parameters.add(parameter);
	}

	@Override
	public List<DemangledDataType> getParameters() {
		return new ArrayList<DemangledDataType>(parameters);
	}

	public String toTemplate() {
		StringBuffer buffer = new StringBuffer();
		buffer.append('<');
		for (int i = 0; i < parameters.size(); ++i) {
			try {
				buffer.append(parameters.get(i).toSignature());
			}
			catch (Error e) {
				Msg.error(this, "Unexpected Error: " + e.getMessage(), e);
			}
			if (i < parameters.size() - 1) {
				buffer.append(',');
			}
		}
		buffer.append('>');
		return buffer.toString();
	}

	@Override
	public String toString() {
		return toTemplate();
	}

	public DataType getDataType(int defaultPointerSize) {
		throw new UnsupportedOperationException(
			"We cannot store templated types in the datatype manager!");
	}
}
