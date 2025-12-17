import { WebTracerProvider, BatchSpanProcessor } from '@opentelemetry/sdk-trace-web'
import { OTLPTraceExporter } from '@opentelemetry/exporter-trace-otlp-http'
import { ZoneContextManager } from '@opentelemetry/context-zone'
import { registerInstrumentations } from '@opentelemetry/instrumentation'
import { FetchInstrumentation } from '@opentelemetry/instrumentation-fetch'
import { resourceFromAttributes } from '@opentelemetry/resources'
import { ATTR_SERVICE_NAME } from '@opentelemetry/semantic-conventions'

const OTEL_ENDPOINT = 'http://localhost:8080/v1/traces'

export function initTracing() {
  const resource = resourceFromAttributes({
    [ATTR_SERVICE_NAME]: 'web-client',
  })

  const exporter = new OTLPTraceExporter({
    url: OTEL_ENDPOINT,
    headers: {},
  })

  const provider = new WebTracerProvider({
    resource,
    spanProcessors: [new BatchSpanProcessor(exporter)],
  })

  provider.register({
    contextManager: new ZoneContextManager(),
  })

  registerInstrumentations({
    instrumentations: [
      new FetchInstrumentation({
        propagateTraceHeaderCorsUrls: [/localhost:8080/],
        clearTimingResources: true,
      }),
    ],
  })

  console.log('OpenTelemetry tracing initialized')
}
