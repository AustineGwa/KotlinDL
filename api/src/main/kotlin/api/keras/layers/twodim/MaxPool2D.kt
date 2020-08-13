package api.keras.layers.twodim

import api.KGraph
import api.keras.layers.Layer
import api.keras.shape.convOutputLength
import org.tensorflow.Operand
import org.tensorflow.Shape
import org.tensorflow.op.Ops

class MaxPool2D<T : Number>(
    val poolSize: IntArray,
    val strides: IntArray,
    val padding: ConvPadding = ConvPadding.SAME,
    name: String = ""
) : Layer<T>(name) {
    override fun defineVariables(tf: Ops, kGraph: KGraph<T>, inputShape: Shape) {}

    override fun computeOutputShape(inputShape: Shape): Shape {
        var rows = inputShape.size(1)
        var cols = inputShape.size(2)
        rows = convOutputLength(
            rows, poolSize[1], padding,
            strides[1]
        )
        cols = convOutputLength(
            cols, poolSize[2], padding,
            strides[2]
        )

        return Shape.make(inputShape.size(0), rows, cols, inputShape.size(3))
    }

    override fun transformInput(tf: Ops, input: Operand<T>): Operand<T> {
        val tfPadding = when (padding) {
            ConvPadding.SAME -> "SAME"
            ConvPadding.VALID -> "VALID"
            ConvPadding.FULL -> "FULL"
        }

        return tf.nn.maxPool(
            input,
            tf.constant(poolSize),
            tf.constant(strides),
            tfPadding
        )
    }

    override fun getWeights(): List<Array<*>> {
        return emptyList()
    }

    override fun hasActivation(): Boolean {
        return false
    }

    override fun getParams(): Int {
        return 0
    }
}