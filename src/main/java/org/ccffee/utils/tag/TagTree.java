package org.ccffee.utils.tag;

import org.ccffee.utils.iteration.ArrayIterator;
import org.ccffee.utils.iteration.ArrayUtils;

import java.util.Iterator;

public class TagTree implements Iterable<Object> {

   private final String name;
   private Object[] tree;
   private TagTree parent;

    public TagTree(){
        tree = new Object[0];
        name = "";
    }
    public TagTree(String name,TagTree parent){
        tree = new Object[0];
        this.name = name;
        this.parent = parent;
    }
    public void add(Object obj){
        tree = ArrayUtils.addAndExpand(tree,obj);
    }
    public void set(int index, Object obj){
        tree[index]=obj;
    }
    public Object get(int i) {
        if(tree.length>i)return tree[i];
        else return null;
    }
    public boolean isTree(int i){
        if(tree.length>i)return tree[i]instanceof TagTree;
        return false;
    }
    @SuppressWarnings("unchecked")
    public <T> T get(int i, Class<T> clazz) {
        Object o = get(i);
        if (clazz.isInstance(o)) return (T) o;
        return null;
    }
    @SuppressWarnings("unused")
    public TagTree getTree(int i) {
        if(isTree(i))return (TagTree) get(i);
        else return null;
    }
    public TagTree getParent(){
        return parent;
    }
    public String getName() {
        return name;
    }
    public Object[] getData(){return tree;}
    @Override
    public String toString() {
        StringBuilder out = new StringBuilder("<" + name + ">");
        for(Object o : tree) out.append(o);
        return out+"</"+name+">";
    }
    public String format(){
        return format("");
    }
    public String format(String tabs){
        StringBuilder out = new StringBuilder(tabs + "<" + name.toUpperCase() + ">\n");
        for(Object o : tree){
            if(o instanceof TagTree)
                out.append(((TagTree) o).format(tabs + "\t")).append("\n");
            else out.append(tabs).append("\t").append(o).append("\n");
        }
        return out+tabs+"</"+name.toUpperCase()+">";
    }
    @SuppressWarnings("unused")
    public static TagTree parse(String s) throws IllegalSyntaxException{
        TagTreeBuilder treeB = new TagTreeBuilder();
        boolean inTag = false;
        StringBuilder current = new StringBuilder();
        boolean ctrl;
        boolean setCtrl = false;
        int ln = 1;
        int col = 0;
        try{
            for(char c : s.toCharArray()){
                col++;
                if(c=='\n'){
                    ln++;
                    col=0;
                }
                ctrl = setCtrl;
                setCtrl = false;
                if(c == '<'&&!ctrl){
                    if(!current.toString().trim().isEmpty()){
                        treeB.add(current.toString().trim());
                    }
                    current = new StringBuilder();
                    inTag=true;
                }else if(c=='>'&&inTag){
                    inTag=false;
                    if(current.toString().startsWith("/"))treeB.closeTag(current.substring(1));
                    else treeB.startTag(current.toString());
                    current = new StringBuilder();
                }else if(ctrl&&c=='n') current.append('\n');
                else if(c=='\\'&&!ctrl)setCtrl=true;
                else current.append(c);
            }
        }catch (IllegalSyntaxException e){
            throw new IllegalSyntaxException(e.getMessage(),e.getType(),ln,col);
        }
        return treeB.build();
    }
    @Override
    public Iterator<Object> iterator() {
        return new ArrayIterator<>(tree);
    }
}