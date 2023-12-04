package serives;
import com.pro.reactive.grpc.models.ReactorTransferServiceGrpc;
import com.pro.reactive.grpc.models.TransferRequest;
import com.pro.reactive.grpc.models.TransferResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class TransferService extends ReactorTransferServiceGrpc.TransferServiceImplBase
{
    @Override
    public Flux<TransferResponse> transfer(Flux<TransferRequest> request) {

     return    request.flatMap(this::process);

    }

    private Mono<TransferResponse> process(TransferRequest req) {


        return null;
    }
}

