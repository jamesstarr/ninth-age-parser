package org.jim.opennlp.classifier

import com.google.common.hash.Hashing
import opennlp.tools.util.ObjectStream
import java.nio.file.Files
import java.nio.file.Path
import java.util.*

class SimpleClassifierCached(
    val dir: Path
) {
    companion object{
        val HashFileName = "Hash"
        val ModelFileName = "Model"
    }

    fun build(
        names:List<String>,
        tokenStream: ObjectStream<SimpleToken>
    ): SimpleClassifier {


        return SimpleClassifier(train(names, tokenStream))
    }


    fun train(names: List<String>, tokenStream: ObjectStream<SimpleToken>): SimpleClassifierModel {
        val path = buildPath(names)
        val cachedHash = getCachedHash(path)
        val th = hashTokens(tokenStream)
        return if(null != cachedHash && Arrays.equals(cachedHash,th)) {
            SimpleClassifierModel.read(path.resolve(ModelFileName))
        } else {
            tokenStream.reset()
            val model = SimpleClassifier.train(tokenStream)
            Files.createDirectories(path)
            model.write(path.resolve(ModelFileName))
            Files.write(path.resolve(HashFileName), th)
            model
        }
    }


    private fun hashTokens (tokenStream: ObjectStream<SimpleToken>): ByteArray {
        val hasher = Hashing.sha256().newHasher()
        tokenStream.reset()
        var token = tokenStream.read()
        while (token!= null) {
            hasher.putString(token.context, Charsets.UTF_8)
            hasher.putString(token.target, Charsets.UTF_8)
            token = tokenStream.read()
        }
        return hasher.hash()!!.asBytes()!!
    }

    private fun getCachedHash(path:Path): ByteArray? {
        val hashFile = path.resolve(HashFileName)
        return if(Files.exists(hashFile)) {
            Files.readAllBytes(hashFile)
        } else {
            null
        }
    }

    fun buildPath(names:List<String>):Path{
        var path = dir;
        for(name in names) {
            path = path.resolve(name)
        }
        return path
    }

}