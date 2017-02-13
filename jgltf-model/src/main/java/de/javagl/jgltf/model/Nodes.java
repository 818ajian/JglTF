/*
 * www.javagl.de - JglTF
 *
 * Copyright 2015-2016 Marco Hutter - http://www.javagl.de
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package de.javagl.jgltf.model;

import de.javagl.jgltf.impl.v1.Node;

/**
 * Utility methods related to {@link Node}s.
 */
class Nodes
{
    /**
     * Compute the local transform of the given {@link Node}. The transform
     * is either taken from the {@link Node#getMatrix()} (if it is not
     * <code>null</code>), or computed from the {@link Node#getTranslation()}, 
     * {@link Node#getRotation()} and {@link Node#getScale()}, if they
     * are not <code>null</code>, respectively.<br>
     * <br>
     * The result will be written to the given array, as a 4x4 matrix in 
     * column major order. If the given array is <code>null</code> or does
     * not have a length of 16, then a new array with length 16 will be 
     * created and returned. 
     * 
     * @param node The node. May not be <code>null</code>.
     * @param result The result array
     * @return The result array
     */
    static float[] computeLocalTransform(
        Node node, float result[])
    {
        float localResult[] = Utils.validate(result, 16);
        if (node.getMatrix() != null)
        {
            float m[] = node.getMatrix();
            System.arraycopy(m, 0, localResult, 0, m.length);
            return localResult;
        }
        
        MathUtils.setIdentity4x4(localResult);
        if (node.getTranslation() != null)
        {
            float t[] = node.getTranslation();
            localResult[12] = t[0]; 
            localResult[13] = t[1]; 
            localResult[14] = t[2]; 
        }
        if (node.getRotation() != null)
        {
            float q[] = node.getRotation();
            float m[] = new float[16];
            MathUtils.quaternionToMatrix4x4(q, m);
            MathUtils.mul4x4(localResult, m, localResult);
        }
        if (node.getScale() != null)
        {
            float s[] = node.getScale();
            float m[] = new float[16];
            m[ 0] = s[0];
            m[ 5] = s[1];
            m[10] = s[2];
            m[15] = 1.0f;
            MathUtils.mul4x4(localResult, m, localResult);
        }
        return localResult;
    }

    /**
     * Private constructor to prevent instantiation
     */
    private Nodes()
    {
        // Private constructor to prevent instantiation
    }
}
