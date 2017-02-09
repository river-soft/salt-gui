export const ping = socket => store => next => action => {
    console.log(`Тип события: ${action.type}, дополнительные данные события: ${action.payload}`);
    socket.emit('action', action);
    return next(action);
};