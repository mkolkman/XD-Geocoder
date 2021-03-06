package core.learning.emperical;

import ca.uwo.csd.ai.nlp.kernel.CustomKernel;
import ca.uwo.csd.ai.nlp.kernel.LinearKernel;
import ca.uwo.csd.ai.nlp.kernel.RBFKernel;
import ca.uwo.csd.ai.nlp.libsvm.svm_parameter;
import core.learning.emperical.setup.ExperimentSetup;
import core.learning.emperical.setup.GazetteerFactory;
import core.learning.emperical.setup.R;
import core.learning.emperical.setup.LglExperimentSetup;
import core.learning.learning_instance.LearningInstance;
import core.learning.evaluator.Metric;
import core.learning.classifier.Classifier;
import core.learning.classifier.svm.MalletSvmClassifierTrainer;
import core.learning.evaluator.Evaluator;
import core.learning.evaluator.lgl.LglEvaluator;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import io.corpus.xml.XMLStreamReaderFactory;
import io.learning_instance.LearningInstanceReader;
import io.learning_instance.LearningInstanceWriter;
import transformers.feature_vector.MalletFeatureVectorTransformer;
import transformers.learning_instance.MalletInstanceTransformer;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class EmpericalSvmClassifierTest extends EmpericalClassifierTest {

    MalletSvmClassifierTrainer trainer;

    @Override
    @Test
    public void testOnLglCorpus() throws Exception, XMLStreamReaderFactory.UnsupportedStreamReaderTypeException {

        ExperimentSetup experimentSetup = new LglExperimentSetup(
                new GazetteerFactory(R.GEONAMES_GAZETTEER_FILE),
                new MaxentTagger(getClass().getResource(R.ENGLISH_TAGGER_MODEL).toString()),
                new LearningInstanceReader(R.LGL_FEATURE_FILE),
                new LearningInstanceWriter(R.LGL_FEATURE_FILE)
        );
        List<LearningInstance> learningInstances = experimentSetup.getLearningInstances(R.LGL_CORPUS_FILE);

        super.populateTrainingAndTestInstanceLists(learningInstances);

        PrintStream out = new PrintStream(new FileOutputStream(R.EXPERIMENT_RESULTS_FILE));
        System.setOut(out);

        double[] gamma_list = new double[]{0.1, 0.25, 0.5, 0.75, 1.0, 2.0};
        double[] cost_list = new double[]{10, 100, 1000};
        double[] weight_b_list = new double[]{60};
        double[] weight_i_list = new double[]{150};
        for (double gamma : gamma_list) {
            for (double cost : cost_list) {
                for (double weight_b : weight_b_list) {
                    for(double weight_i : weight_i_list) {
                        System.out.print(gamma + ", ");
                        System.out.print(cost + ", ");
                        System.out.print(weight_b + ", ");
                        System.out.print(weight_i + ", ");
                        System.out.print(1.0 + ", ");

                        List<Metric> metrics = doExperiment(gamma, cost, weight_b, weight_i, 1.0);

                        super.printPerformanceMetricsWithoutHeader(metrics);
                    }
                }
            }
        }
    }

    @Override
    public List<Metric> doExperiment(double gamma, double cost, double weight_b, double weight_i, double weight_o) {
        trainer = makeClassifierTrainer(makeSvmKernel(gamma, cost));

        svm_parameter params = makeTrainingParams(weight_b, weight_i, weight_o);

        Classifier classifier = trainer.train(trainingInstances, params);

        Evaluator evaluator = new LglEvaluator(classifier.trial(testInstances));

        return evaluator.getPerformanceMetrics();
    }

    public MalletSvmClassifierTrainer makeClassifierTrainer(CustomKernel kernel) {
        MalletInstanceTransformer instanceTransformer = new MalletInstanceTransformer(new MalletFeatureVectorTransformer());

        return new MalletSvmClassifierTrainer(kernel, instanceTransformer);
    }

    private CustomKernel makeSvmKernel(double gamma, double cost) {
        List<CustomKernel> kernels = new ArrayList<CustomKernel>();
        kernels.add(new LinearKernel());
        svm_parameter kernelParams = new svm_parameter();
        kernelParams.gamma = gamma;
        kernelParams.C = cost;
        kernels.add(new RBFKernel(kernelParams));

        return new ca.uwo.csd.ai.nlp.kernel.CompositeKernel(kernels);
    }

    private svm_parameter makeTrainingParams(double weightB, double weightI, double weightO) {
        svm_parameter params = new svm_parameter();
        params.weight_label = new int[]{1, 2, 3};
        params.weight = new double[]{weightB, weightO, weightI};
        params.nr_weight = 3;
        return params;
    }

}
