function getRequiredComponents()
    return {}
end

function renderStart()
  game.camera:applyTransforms(g:getContext())
end

function renderEnd()
  game.camera:resetTransforms(g:getContext())
end