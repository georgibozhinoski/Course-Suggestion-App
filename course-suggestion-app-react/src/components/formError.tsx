import { Alert, AlertDescription, AlertTitle } from "@/components/ui/alert"

export function FormError({ message }: { message: string }) {
  return (
    <Alert variant="destructive">
      <AlertTitle>Error</AlertTitle>
      <AlertDescription>
        {message}
      </AlertDescription>
    </Alert>
  )
}
