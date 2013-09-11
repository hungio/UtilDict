package module;

import java.util.List;

public interface UtilDict {
	public String getDictKind();
	public String getDictName();
	public String lookUp(String word);
	public List <Word> Search(String word);
}
