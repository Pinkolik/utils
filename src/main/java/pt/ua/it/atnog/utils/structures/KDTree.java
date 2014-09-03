package pt.ua.it.atnog.utils.structures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KDTree<T extends Point> {
    private int maxDim, size;
    private KDNode<T> root;

    public KDTree(int maxDim) {
        this(null, maxDim, 0);
    }

    private KDTree(KDNode<T> root, int maxDim, int size) {
        this.root = root;
        this.maxDim = maxDim;
        this.size = size;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        root = null;
        size = 0;
    }

    public void add(T p) {
        root = add(p, root, 0);
        size++;
    }

    private KDNode<T> add(T target, KDNode<T> node, int cd) {
        if (node == null)
            node = new KDNode<T>(target);
        else if (node.data.equals(target))
            System.err.println("error! duplicate");
        else if (target.coor(cd) < node.data.coor(cd))
            node.left = add(target, node.left, (cd + 1) % maxDim);
        else
            node.right = add(target, node.right, (cd + 1) % maxDim);
        return node;
    }

    public void remove(T p) {
        root = remove(p, root, 0);
        size--;
    }

    private KDNode<T> remove(T p, KDNode<T> node, int cd) {
        if (node != null) {
            if (node.data.equals(p)) {
                if (node.right != null) {
                    node.data = findMin(node.right, cd, (cd + 1) % maxDim).data;
                    node.right = remove(node.data, node.right, (cd + 1)
                            % maxDim);
                } else if (node.left != null) {
                    node.data = findMin(node.left, cd, (cd + 1) % maxDim).data;
                    node.right = remove(node.data, node.left, (cd + 1) % maxDim);
                    node.left = null;
                } else
                    node = null;
            } else if (p.coor(cd) < node.data.coor(cd))
                node.left = remove(p, node.left, (cd + 1) % maxDim);
            else
                node.right = remove(p, node.right, (cd + 1) % maxDim);
        }

        return node;
    }

    private KDNode<T> parent(Point p) {
        if (root != null)
            return parent(p, root, 0);
        else
            return null;
    }

    private KDNode<T> parent(Point p, KDNode<T> node, int cd) {
        if (p.coor(cd) < node.data.coor(cd)) {
            if (node.left == null)
                return node;
            else
                return parent(p, node.left, (cd + 1) % maxDim);
        } else {
            if (node.right == null)
                return node;
            else
                return parent(p, node.right, (cd + 1) % maxDim);
        }
    }

    public List<T> closer(T target, double dist) {
        List<T> list = new ArrayList<T>();
        closer(root, target, dist, list, 0);
        return list;
    }

    private void closer(KDNode<T> node, T target, double dist, List<T> list,
                        int cd) {
        if (node != null) {
            if (target.distance(node.data) < dist)
                list.add(node.data);
            double dp = Math.abs(node.data.coor(cd) - target.coor(cd));
            if (dp < dist) {
                closer(node.left, target, dist, list, (cd + 1) % maxDim);
                closer(node.right, target, dist, list, (cd + 1) % maxDim);
            } else if (target.coor(cd) < node.data.coor(cd))
                closer(node.left, target, dist, list, (cd + 1) % maxDim);
            else
                closer(node.right, target, dist, list, (cd + 1) % maxDim);
        }
    }

    public T nearest(T target) {
        T rv = null;

        if (root != null) {
            KDNode<T> parent = parent(target);
            double dist = target.distance(parent.data);
            KDNode<T> bestOne = nearest(root, dist, target, 0);
            if (bestOne != null)
                rv = bestOne.data;
            else
                rv = parent.data;
        }

        return rv;
    }

    private KDNode<T> nearest(KDNode<T> node, double dist,
                              Point target, int cd) {
        KDNode<T> result = null;
        if(node != null) {
            if (target.distance(node.data) < dist) {
                result = node;
                dist = target.distance(result.data);
            }
            double dp = Math.abs(node.data.coor(cd) - target.coor(cd));
            if (dp < dist) {
                KDNode<T> temp = null;
                temp = nearest(node.left, dist, target, (cd + 1) % maxDim);
                if(temp != null) {
                    result = temp;
                    dist = target.distance(result.data);
                }
                temp = nearest(node.right, dist, target, (cd + 1) % maxDim);
                if (temp != null ) {
                    result = temp;
                    dist = target.distance(result.data);
                }
            } else {
                if (target.coor(cd) < node.data.coor(cd)) {
                    if (node.left != null)
                        result = nearest(node.left, dist, target, (cd + 1)% maxDim);
                } else {
                    if (node.right != null)
                        result = nearest(node.right, dist, target, (cd + 1)% maxDim);
                }
            }
        }
        return result;
    }

    public KDNode<T> findMin(int dim) {
        return findMin(root, dim, 0);
    }

    private KDNode<T> findMin(KDNode<T> node, int dim, int cd) {
        KDNode<T> result = null;

        if (node != null) {
            if (cd == dim) {
                result = minimim(findMin(node.left, dim, (cd + 1) % maxDim),
                        node, dim);
            } else {
                result = minimum(findMin(node.left, dim, (cd + 1) % maxDim),
                        findMin(node.right, dim, (cd + 1) % maxDim), node, dim);
            }
        }
        return result;
    }

    private KDNode<T> minimim(KDNode<T> a, KDNode<T> b, int dim) {
        KDNode<T> min = null;

        if (a == null)
            min = b;
        else {
            if (b.data.coor(dim) < a.data.coor(dim))
                min = b;
            else
                min = a;
        }

        return min;
    }

    private KDNode<T> minimum(KDNode<T> a, KDNode<T> b, KDNode<T> c, int dim) {
        KDNode<T> min = null;

        if (a == null)
            min = b;
        else if (b == null)
            min = a;
        else {
            if (a.data.coor(dim) < b.data.coor(dim))
                min = a;
            else
                min = b;
        }

        if (min == null)
            min = c;
        else {
            if (c.data.coor(dim) < min.data.coor(dim))
                min = c;
        }

        return min;
    }

    public boolean contains(Point target) {
        boolean rv = false;

        if(root != null)
            rv = contains(root, target, 0);

        return rv;
    }

    private boolean contains(KDNode<T> node, Point target, int cd) {
        boolean rv = false;

        if (node != null) {
            if (node.data.equals(target))
                rv = true;
            else if (target.coor(cd) < node.data.coor(cd)) {
                rv = contains(node.left, target, (cd + 1) % maxDim);
            } else {
                rv = contains(node.right, target, (cd + 1) % maxDim);
            }
        }

        return rv;
    }

    private void print(KDNode<T> node, StringBuilder sb, int level) {
        if (node != null) {
            sb.append("Level: " + level + " -> " + node.toString() + "\n");
            print(node.left, sb, level + 1);
            print(node.right, sb, level + 1);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        print(root, sb, 0);
        return sb.toString();
    }

    public static <T extends Comparable<T>> int compare(T t1, T t2) {
        return t1.compareTo(t2);
    }

    public static <T extends Point> KDTree<T> build(List<T> points) {
        return new KDTree<T>(build(points, 0, 2), 2, points.size());
    }

    private static <T extends Point> KDNode<T> build(List<T> elements,
                                                         int cd, int maxDim) {
        KDNode<T> node = null;

        if (elements.size() > 1) {
            Collections.sort(elements, new KDElementComparator(cd));
            int split = split(elements, cd);
            node = new KDNode<T>(elements.get(split));
            node.left = build(elements.subList(0, split), (cd + 1) % maxDim,
                    maxDim);
            node.right = build(elements.subList(split + 1, elements.size()),
                    (cd + 1) % maxDim, maxDim);
        } else if (elements.size() == 1)
            node = new KDNode<T>(elements.get(0));

        return node;
    }

    private static <T extends Point> int split(List<T> elements, int cd) {
        int mid = elements.size() / 2, split = mid;
        for (; split > 0 && elements.get(split-1).coor(cd) == elements.get(mid).coor(cd); split--);
        return split;
    }
}
