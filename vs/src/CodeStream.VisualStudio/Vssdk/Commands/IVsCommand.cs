﻿using System.Threading.Tasks;

namespace CodeStream.VisualStudio.Vssdk.Commands
{
    /// <summary>
    /// Represents a Visual Studio command that does not accept a parameter.
    /// </summary>
    public interface IVsCommand : IVsCommandBase
    {
        /// <summary>
        /// Executes the command.
        /// </summary>
        /// <returns>A task that tracks the execution of the command.</returns>
        Task Execute();
    }

    /// <summary>
    /// Represents a Visual Studio command that accepts a parameter.
    /// </summary>
    public interface IVsCommand<TParam> : IVsCommandBase
    {
        /// <summary>
        /// Executes the command.
        /// </summary>
        /// <param name="parameter">The command parameter.</param>
        /// <returns>A task that tracks the execution of the command.</returns>
        System.Threading.Tasks.Task Execute(TParam parameter);
    }
}