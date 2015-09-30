package android.support.v4.content;

import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

abstract class ModernAsyncTask<Params, Progress, Result>
{
  private static final int CORE_POOL_SIZE = 5;
  private static final int KEEP_ALIVE = 1;
  private static final String LOG_TAG = "AsyncTask";
  private static final int MAXIMUM_POOL_SIZE = 128;
  private static final int MESSAGE_POST_PROGRESS = 2;
  private static final int MESSAGE_POST_RESULT = 1;
  public static final Executor THREAD_POOL_EXECUTOR;
  private static volatile Executor sDefaultExecutor;
  private static final InternalHandler sHandler;
  private static final BlockingQueue<Runnable> sPoolWorkQueue;
  private static final ThreadFactory sThreadFactory = new ThreadFactory()
  {
    private final AtomicInteger mCount = new AtomicInteger(1);

    public Thread newThread(Runnable paramRunnable)
    {
      return new Thread(paramRunnable, "ModernAsyncTask #" + this.mCount.getAndIncrement());
    }
  };
  private final FutureTask<Result> mFuture = new FutureTask(this.mWorker)
  {
    protected void done()
    {
      try
      {
        Object localObject = get();
        ModernAsyncTask.this.postResultIfNotInvoked(localObject);
        return;
      }
      catch (InterruptedException localInterruptedException)
      {
        Log.w("AsyncTask", localInterruptedException);
      }
      catch (ExecutionException localExecutionException)
      {
        throw new RuntimeException("An error occured while executing doInBackground()", localExecutionException.getCause());
      }
      catch (CancellationException localCancellationException)
      {
        ModernAsyncTask.this.postResultIfNotInvoked(null);
      }
      catch (Throwable localThrowable)
      {
        throw new RuntimeException("An error occured while executing doInBackground()", localThrowable);
      }
    }
  };
  private volatile Status mStatus = Status.PENDING;
  private final AtomicBoolean mTaskInvoked = new AtomicBoolean();
  private final WorkerRunnable<Params, Result> mWorker = new WorkerRunnable()
  {
    public Result call()
      throws Exception
    {
      ModernAsyncTask.this.mTaskInvoked.set(true);
      Process.setThreadPriority(10);
      return ModernAsyncTask.this.postResult(ModernAsyncTask.this.doInBackground(this.mParams));
    }
  };

  static
  {
    sPoolWorkQueue = new LinkedBlockingQueue(10);
    THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(5, 128, 1L, TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory);
    sHandler = new InternalHandler(null);
    sDefaultExecutor = THREAD_POOL_EXECUTOR;
  }

  public static void execute(Runnable paramRunnable)
  {
    sDefaultExecutor.execute(paramRunnable);
  }

  private void finish(Result paramResult)
  {
    if (isCancelled())
      onCancelled(paramResult);
    while (true)
    {
      this.mStatus = Status.FINISHED;
      return;
      onPostExecute(paramResult);
    }
  }

  public static void init()
  {
    sHandler.getLooper();
  }

  private Result postResult(Result paramResult)
  {
    InternalHandler localInternalHandler = sHandler;
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = paramResult;
    localInternalHandler.obtainMessage(1, new AsyncTaskResult(this, arrayOfObject)).sendToTarget();
    return paramResult;
  }

  private void postResultIfNotInvoked(Result paramResult)
  {
    if (this.mTaskInvoked.get())
      return;
    postResult(paramResult);
  }

  public static void setDefaultExecutor(Executor paramExecutor)
  {
    sDefaultExecutor = paramExecutor;
  }

  public final boolean cancel(boolean paramBoolean)
  {
    return this.mFuture.cancel(paramBoolean);
  }

  protected abstract Result doInBackground(Params[] paramArrayOfParams);

  public final ModernAsyncTask<Params, Progress, Result> execute(Params[] paramArrayOfParams)
  {
    return executeOnExecutor(sDefaultExecutor, paramArrayOfParams);
  }

  public final ModernAsyncTask<Params, Progress, Result> executeOnExecutor(Executor paramExecutor, Params[] paramArrayOfParams)
  {
    if (this.mStatus != Status.PENDING);
    switch (4.$SwitchMap$android$support$v4$content$ModernAsyncTask$Status[this.mStatus.ordinal()])
    {
    default:
      this.mStatus = Status.RUNNING;
      onPreExecute();
      this.mWorker.mParams = paramArrayOfParams;
      paramExecutor.execute(this.mFuture);
      return this;
    case 1:
      throw new IllegalStateException("Cannot execute task: the task is already running.");
    case 2:
    }
    throw new IllegalStateException("Cannot execute task: the task has already been executed (a task can be executed only once)");
  }

  public final Result get()
    throws InterruptedException, ExecutionException
  {
    return this.mFuture.get();
  }

  public final Result get(long paramLong, TimeUnit paramTimeUnit)
    throws InterruptedException, ExecutionException, TimeoutException
  {
    return this.mFuture.get(paramLong, paramTimeUnit);
  }

  public final Status getStatus()
  {
    return this.mStatus;
  }

  public final boolean isCancelled()
  {
    return this.mFuture.isCancelled();
  }

  protected void onCancelled()
  {
  }

  protected void onCancelled(Result paramResult)
  {
    onCancelled();
  }

  protected void onPostExecute(Result paramResult)
  {
  }

  protected void onPreExecute()
  {
  }

  protected void onProgressUpdate(Progress[] paramArrayOfProgress)
  {
  }

  protected final void publishProgress(Progress[] paramArrayOfProgress)
  {
    if (isCancelled())
      return;
    sHandler.obtainMessage(2, new AsyncTaskResult(this, paramArrayOfProgress)).sendToTarget();
  }

  private static class AsyncTaskResult<Data>
  {
    final Data[] mData;
    final ModernAsyncTask mTask;

    AsyncTaskResult(ModernAsyncTask paramModernAsyncTask, Data[] paramArrayOfData)
    {
      this.mTask = paramModernAsyncTask;
      this.mData = paramArrayOfData;
    }
  }

  private static class InternalHandler extends Handler
  {
    public void handleMessage(Message paramMessage)
    {
      ModernAsyncTask.AsyncTaskResult localAsyncTaskResult = (ModernAsyncTask.AsyncTaskResult)paramMessage.obj;
      switch (paramMessage.what)
      {
      default:
      case 1:
      case 2:
      }
      while (true)
      {
        return;
        localAsyncTaskResult.mTask.finish(localAsyncTaskResult.mData[0]);
        continue;
        localAsyncTaskResult.mTask.onProgressUpdate(localAsyncTaskResult.mData);
      }
    }
  }

  public static enum Status
  {
    static
    {
      FINISHED = new Status("FINISHED", 2);
      Status[] arrayOfStatus = new Status[3];
      arrayOfStatus[0] = PENDING;
      arrayOfStatus[1] = RUNNING;
      arrayOfStatus[2] = FINISHED;
      $VALUES = arrayOfStatus;
    }
  }

  private static abstract class WorkerRunnable<Params, Result>
    implements Callable<Result>
  {
    Params[] mParams;
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     android.support.v4.content.ModernAsyncTask
 * JD-Core Version:    0.5.4
 */